package com.polycom.hst.wechat.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.polycom.hst.wechat.model.WechatSnsToken;
import com.polycom.hst.wechat.service.Hst2MSAuthService;
import com.polycom.hst.wechat.service.WechatOauth2AccessService;
import com.polycom.hst.wechat.virtual.AbstractAutowireAware;
import static com.polycom.hst.wechat.constant.WechatConstant.*;

public class HstMsAuthFilter extends AbstractAutowireAware implements Filter {
	private static final Logger log = LoggerFactory.getLogger(HstMsAuthFilter.class);
	@Autowired
	private WechatOauth2AccessService wechatOauth2AccessService;

	@Autowired
	private Hst2MSAuthService hst2MSAuthService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		autowireSpringContext(filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String openid = "";
		String code = req.getParameter(CODE_PARAM);
		String msUrl = "";
		Object wechatObj = req.getSession().getAttribute(WECHAT_SESSION_MAP);
		int start = req.getRequestURI().indexOf(MS_REDIRECT_URL_PARAM);
		if (start != -1) {
			msUrl = URLDecoder.decode(req.getRequestURI().substring(start + MS_REDIRECT_URL_PARAM.length() + 1), UTF8);
		}

		if (wechatObj instanceof Map && ((Map<?, ?>) wechatObj).containsKey(OPEN_ID_PARAM)) {
			// whether openid is saved in session.
			openid = (String) ((Map<?, ?>) wechatObj).get(OPEN_ID_PARAM);
		} else if (!Strings.isNullOrEmpty(code) && wechatOauth2AccessService != null) {
			// whether code is authorized by wechat server.
			WechatSnsToken token = wechatOauth2AccessService.getAccessTokensByCode(code);
			//TODO
			//Test
			token.setOpenid("13");
			openid = token.getOpenid();
			if (!Strings.isNullOrEmpty(openid)) {
				Map<String, String> wechatMap = Maps.newHashMap();
				wechatMap.put(OPEN_ID_PARAM, openid);
				wechatMap.put(USER_ACCESS_TOKEN, token.getAccess_token());
				wechatMap.put(USER_REFRESH_TOKEN, token.getRefresh_token());
				wechatMap.put(USER_UNION_ID, token.getUnionid());
				wechatMap.put(EXPIRED_TIME, token.getExpires_in() == null ? null
						: String.valueOf((new Date().getTime() + token.getExpires_in() * 1000L)));
				// save wechat authorization info.
				req.getSession().setAttribute(WECHAT_SESSION_MAP, wechatMap);
			}
		}

		log.info("current open is: ({})", openid);
		log.info("ms url is: ({})", msUrl);
		
		if (hst2MSAuthService != null && !Strings.isNullOrEmpty(openid) && !Strings.isNullOrEmpty(msUrl)) {
			// use open id to authenticate media suite site.
			if (hst2MSAuthService.isAuthByOpenid(openid)) {
				// redirect target to access Media Suite.
				/*WechatPlcmLoginResult result = hst2MSAuthService.login(openid);
				if (!Strings.isNullOrEmpty(result.getToken())) {
					//write cookie
					HttpKit.addCookie(req, resp, result.getCookieDomain(), result.getContextPath(), TOKEN,
							result.getToken());
					
					try {
						URI msUri = HttpKit.appendQuery(msUrl, String.format("token=%s&userId=%s", result.getToken(),
								MSUtils.getUserId(result.getUser())));
						msUrl = msUri.toString();
					} catch (URISyntaxException e) {
						// URL Syntax error, need to show error page.
						RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
						dispatcher.forward(request, response);
					}
					log.info("real media suite url is: ({})", msUrl);
					resp.sendRedirect(resp.encodeRedirectURL(msUrl));
				} else {
					// login failure, need to show error page.
					RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
					dispatcher.forward(request, response);
				}*/
				String ssoLoginUrl = hst2MSAuthService.ssoLogin(openid, msUrl);
				log.info("ms sso login url is: ({})", ssoLoginUrl);
				if (!Strings.isNullOrEmpty(ssoLoginUrl)) {
					try {
						new URI(ssoLoginUrl);
						resp.sendRedirect(resp.encodeRedirectURL(ssoLoginUrl));
					} catch (URISyntaxException e) {
						// SSO URL Syntax error, need to show error page.
						RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
						dispatcher.forward(request, response);
					}

				} else {
					// SSO URL Syntax error, need to show error page.
					RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
					dispatcher.forward(request, response);
				}
				
				
			} else {
				// need to bind wechat
				RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
				// set target url for login page.
				request.setAttribute(MS_REDIRECT_URL_PARAM, msUrl);
				dispatcher.forward(request, response);
			}
		} else {
			// no open id, need to show error page.
			RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
			dispatcher.forward(request, response);
		}
	}

	@Override
	public void destroy() {
		if (wechatOauth2AccessService != null)
			wechatOauth2AccessService = null;
	}

}

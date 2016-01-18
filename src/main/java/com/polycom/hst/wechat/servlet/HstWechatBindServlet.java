package com.polycom.hst.wechat.servlet;

import static com.polycom.hst.wechat.constant.WechatConstant.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.polycom.hst.wechat.model.WechatBindResult;
import com.polycom.hst.wechat.model.WechatPlcmLoginResult;
import com.polycom.hst.wechat.service.Hst2MSAuthService;
import com.polycom.hst.wechat.utils.HttpKit;
import com.polycom.hst.wechat.virtual.AbstractAutowireHttpServlet;

public class HstWechatBindServlet extends AbstractAutowireHttpServlet {
	private static final Logger log = LoggerFactory.getLogger(HstWechatBindServlet.class);
	private static final long serialVersionUID = 1L;
	@Autowired
	private Hst2MSAuthService hst2MSAuthService;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String account = req.getParameter("account");
		String password = req.getParameter("password");
		String msUrl = req.getParameter("msurl");
		String openid = "";

		Object wechatObj = req.getSession().getAttribute(WECHAT_SESSION_MAP);

		if (wechatObj instanceof Map && ((Map<?, ?>) wechatObj).containsKey(OPEN_ID_PARAM)) {
			// whether openid is saved in session.
			openid = (String) ((Map<?, ?>) wechatObj).get(OPEN_ID_PARAM);
		}

		log.info("use this openid: ({}) to bind ms.", openid);
		log.info("ms url is: ({})", msUrl);
		
		resp.setContentType("application/json;charset=utf8");
		
		if (!Strings.isNullOrEmpty(openid) && hst2MSAuthService != null) {
			//binding ms account
			WechatPlcmLoginResult result = hst2MSAuthService.login(openid, account, password);
			//resp.sendRedirect(resp.encodeRedirectURL(msUrl));
			if (!Strings.isNullOrEmpty(result.getToken())) {
				resp.setStatus(HttpServletResponse.SC_OK);
				
				// write cookie
				HttpKit.addCookie(req, resp, result.getCookieDomain(), result.getContextPath(), TOKEN,
						result.getToken());
				// response json to client.
				resp.getWriter()
						.print((new Gson()).toJson(new WechatBindResult(msUrl, result.getToken(),
								result.getUser() == null ? null : result.getUser().getLoginName(),
								result.getUser() == null ? null : result.getUser().getDomainName(),
								result.getUser() == null ? null : result.getUser().getUserType(),
								result.getCookieDomain(), result.getContextPath())));
			} else {
				// Media suite login failure.
				if (!Strings.isNullOrEmpty(result.getErrorCode())) {
					try {
						Integer code = Integer.valueOf(result.getErrorCode());
						resp.setStatus(code);
					} catch (NumberFormatException e) {
						resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
					}
				} else {
					resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
				}
			}

		} else {
			// no open id, need to show error page.
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}

	}
	
	@Override
	public void destroy() {
		if (hst2MSAuthService != null)
			hst2MSAuthService = null;
	}
}

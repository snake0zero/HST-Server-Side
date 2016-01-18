package com.polycom.hst.wechat.virtual;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.base.Strings;
import static com.polycom.hst.wechat.constant.WechatConstant.*;
import com.polycom.hst.wechat.constant.WechatAuthType;
import com.polycom.hst.wechat.service.WechatUrlService;

abstract public class AbstractWechatAuthFilter extends AbstractAutowireAware implements Filter {
	private static final Logger log = LoggerFactory.getLogger(AbstractWechatAuthFilter.class);
	@Autowired
	private WechatUrlService wechatUrlService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		autowireSpringContext(filterConfig.getServletContext());
	}

	protected void authorizeViaWechat(HttpServletResponse httpResp, WechatAuthType type, String redirectUri)
			throws IOException {
		if (wechatUrlService != null && !Strings.isNullOrEmpty(redirectUri)) {
			redirectUri = URLEncoder.encode(redirectUri, UTF8);
			String url = wechatUrlService.getWechatAuthUrl(type, redirectUri);
			log.info("access wechat via: ({})", url);
			if (!Strings.isNullOrEmpty(url))
				httpResp.sendRedirect(url);
		}
	}

	protected String getHST2MSAuthUrl(HttpServletRequest httpReq, HttpServletResponse httpResp, String redirectUri)
			throws UnsupportedEncodingException {
		if (!Strings.isNullOrEmpty(redirectUri)) {
			String uri = httpReq.getScheme() + "://" + httpReq.getServerName() + ":" + httpReq.getServerPort()
					+ httpReq.getContextPath() + AUTH_MS_SERVLET + "/" + MS_REDIRECT_URL_PARAM + "="
					+ URLEncoder.encode(URLDecoder.decode(redirectUri, UTF8), UTF8).replaceAll("%2F", "/");
			return httpResp.encodeRedirectURL(uri);
		} else {
			return "";
		}
	}

	@Override
	public void destroy() {
		if (wechatUrlService != null)
			wechatUrlService = null;
	}
}

package com.polycom.hst.wechat.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.polycom.hst.wechat.constant.WechatAuthType.*;
import static com.polycom.hst.wechat.constant.WechatConstant.*;
import com.polycom.hst.wechat.virtual.AbstractWechatAuthFilter;

public class HstWechatAuthzBaseFilter extends AbstractWechatAuthFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Boolean isGet = req.getMethod().toUpperCase().equals("GET");
		if (isGet) {
			authorizeViaWechat(resp, base, getHST2MSAuthUrl(req, resp, req.getParameter(MS_REDIRECT_URL_PARAM)));
		}
	}
}

package com.polycom.hst.wechat.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.polycom.hst.wechat.service.WechatConfigService;
import com.polycom.hst.wechat.virtual.AbstractAutowireAware;

public class HstWechatJoinFilter extends AbstractAutowireAware implements Filter {
	private static final Logger log = LoggerFactory.getLogger(HstWechatJoinFilter.class);
	@Autowired
	private WechatConfigService wechatConfigService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		autowireSpringContext(filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//TODO
		HttpServletRequest req = (HttpServletRequest) request;

		
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		if (wechatConfigService != null)
			wechatConfigService = null;

	}

}

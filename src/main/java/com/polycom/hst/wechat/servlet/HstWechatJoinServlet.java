package com.polycom.hst.wechat.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.polycom.hst.wechat.constant.WechatConfigKey;
import com.polycom.hst.wechat.service.WechatConfigService;
import com.polycom.hst.wechat.utils.WechatUtils;
import com.polycom.hst.wechat.virtual.AbstractAutowireHttpServlet;

public class HstWechatJoinServlet extends AbstractAutowireHttpServlet {
	private static final Logger log = LoggerFactory.getLogger(HstWechatJoinServlet.class);
	private static final long serialVersionUID = 1L;
	@Autowired
	private WechatConfigService wechatConfigService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr  = req.getParameter("echostr");
		String token = "";
		log.info("Join wechat as below:");
		log.info("signature: {}", signature);
		log.info("timestamp: {}", timestamp);
		log.info("nonce: {}", nonce);
		log.info("echostr: {}", echostr);
		if(wechatConfigService != null)
			token = wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_TOKEN);
		log.info("token: {}", token);
		
		//开发者通过检验signature对请求进行校验（下面有校验方式）。若确认此次GET请求来自微信服务器，
		//请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。
		if(WechatUtils.checkSignature(token, signature, timestamp, nonce))
			resp.getWriter().write(echostr);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//TODO
		//Send and receive message.
	}
	
	@Override
	public void destroy() {
		if (wechatConfigService != null)
			wechatConfigService = null;
	}
    

}

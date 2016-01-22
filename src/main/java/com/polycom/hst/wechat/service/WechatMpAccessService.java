package com.polycom.hst.wechat.service;

import javax.servlet.ServletRequest;

import com.polycom.hst.wechat.model.WechatJsApiConfig;
import com.polycom.hst.wechat.model.WechatMpMenu;

public interface WechatMpAccessService {
	public boolean getAccessToken();
	public boolean createMenu();
	public boolean deleteMenu();
	public WechatMpMenu queryMenu();
	public WechatJsApiConfig getJsApiConfig(StringBuffer url, ServletRequest request);
}

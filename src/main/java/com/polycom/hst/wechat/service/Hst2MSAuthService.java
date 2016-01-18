package com.polycom.hst.wechat.service;

import com.polycom.hst.wechat.model.WechatPlcmLoginResult;

public interface Hst2MSAuthService {
	public boolean isAuthByOpenid(String openid);
	public boolean saveAuthInfo(String openid, String userid);
	public String ssoLogin(String openid, String msUrl);
	public WechatPlcmLoginResult login(String openid, String account, String password);
	public boolean deleteAuthByOpenid(String openid);
}

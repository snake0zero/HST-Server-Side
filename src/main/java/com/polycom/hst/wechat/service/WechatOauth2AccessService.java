package com.polycom.hst.wechat.service;

import com.polycom.hst.wechat.model.WechatSnsToken;
import com.polycom.hst.wechat.model.WechatUserinfo;

public interface WechatOauth2AccessService {
	public WechatSnsToken getAccessTokensByCode(String code);
	
	public WechatSnsToken refreshAccessTokens(String refreshToken);
	
	public WechatSnsToken checkAccessTokens(String accessToken, String openId);
	
	public WechatUserinfo getWechatUserinfo(String accessToken, String openId);
}

package com.polycom.hst.wechat.service;

import com.polycom.hst.wechat.constant.WechatAuthType;

public interface WechatUrlService {
	public String getWechatAuthUrl(WechatAuthType type, String redirectUri);
}

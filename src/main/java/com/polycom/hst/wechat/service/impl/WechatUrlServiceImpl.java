package com.polycom.hst.wechat.service.impl;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.polycom.hst.wechat.constant.WechatAuthType;
import com.polycom.hst.wechat.constant.WechatConfigKey;
import com.polycom.hst.wechat.service.WechatConfigService;
import com.polycom.hst.wechat.service.WechatUrlService;

@Service
public class WechatUrlServiceImpl implements WechatUrlService {
	@Autowired
	private WechatConfigService wechatConfigService;

	@Override
	public String getWechatAuthUrl(WechatAuthType type, String redirectUri) {
		String url = "";
		switch (type) {
		case base:
			url = WechatUrl.AUTH_BASE_CODE_URL;
			break;
		case user:
			url = WechatUrl.AUTH_USER_CODE_URL;
			break;
		case login:
			url = WechatUrl.AUTH_LOGIN_CODE_URL;
			break;
		case qrcode:
			url = WechatUrl.AUTH_QRCODE_CODE_URL;
			break;
		default:
			break;
		}
		if (!Strings.isNullOrEmpty(url) && wechatConfigService != null)
			return String.format(url, wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_OPEN_APPID),
					redirectUri);
		else
			return null;
	}
	
	@PreDestroy
	private void destory() {
		if(wechatConfigService != null)
			wechatConfigService = null;
	}

}

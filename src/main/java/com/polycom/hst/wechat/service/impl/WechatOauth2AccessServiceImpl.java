package com.polycom.hst.wechat.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polycom.hst.wechat.utils.HttpKit;
import com.google.gson.Gson;
import com.polycom.hst.wechat.constant.WechatConfigKey;
import com.polycom.hst.wechat.model.WechatSnsToken;
import com.polycom.hst.wechat.model.WechatUserinfo;
import com.polycom.hst.wechat.service.WechatConfigService;
import com.polycom.hst.wechat.service.WechatOauth2AccessService;

@Service
public class WechatOauth2AccessServiceImpl implements WechatOauth2AccessService {
	private static final Logger log = LoggerFactory.getLogger(WechatOauth2AccessServiceImpl.class);
	@Autowired
	private WechatConfigService wechatConfigService;

	@Override
	public WechatSnsToken getAccessTokensByCode(String code) {
		if (wechatConfigService == null)
			return new WechatSnsToken();

		String url = String.format(WechatUrl.GET_USER_TOKEN_URL,
				wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_APPID),
				wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_APPSECRET), code);

		try {
			String tokenStr = HttpKit.get(url);
			log.info("response for getting access token: ({})", tokenStr);
			return new Gson().fromJson(tokenStr, WechatSnsToken.class);

		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			log.error("Exception when getting access token by code: {}", e);
			return new WechatSnsToken();
		}
	}

	@Override
	public WechatSnsToken refreshAccessTokens(String refreshToken) {
		if (wechatConfigService == null)
			new WechatSnsToken();

		String url = String.format(WechatUrl.REFRESH_USER_TOKEN_URL,
				wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_APPID), refreshToken);

		try {
			String tokenStr = HttpKit.get(url);
			log.info("response for refreshing access token: ({})", tokenStr);
			return new Gson().fromJson(tokenStr, WechatSnsToken.class);

		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			log.error("Exception when refreshing access token by code: {}", e);
			return new WechatSnsToken();
		}
	}

	@Override
	public WechatSnsToken checkAccessTokens(String accessToken, String openId) {
		String url = String.format(WechatUrl.CHECK_USER_TOKEN_URL, accessToken, openId);

		try {
			String tokenStr = HttpKit.get(url);
			log.info("response for checking access token: ({})", tokenStr);
			return new Gson().fromJson(tokenStr, WechatSnsToken.class);

		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			log.error("Exception when checking access token by code: {}", e);
			return new WechatSnsToken();
		}
	}

	@Override
	public WechatUserinfo getWechatUserinfo(String accessToken, String openId) {
		String url = String.format(WechatUrl.USER_INFO_URL, accessToken, openId);

		try {
			String tokenStr = HttpKit.get(url);
			log.info("response for getting user info: ({})", tokenStr);
			return new Gson().fromJson(tokenStr, WechatUserinfo.class);

		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			log.error("Exception when getting user info: {}", e);
			return new WechatUserinfo();
		}
	}

	@PreDestroy
	private void destory() {
		if (wechatConfigService != null)
			wechatConfigService = null;
	}
}

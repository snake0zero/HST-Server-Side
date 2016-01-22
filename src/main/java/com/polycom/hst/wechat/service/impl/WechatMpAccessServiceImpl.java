package com.polycom.hst.wechat.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Enumeration;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.polycom.hst.wechat.constant.WechatConfigKey;
import com.polycom.hst.wechat.model.WechatJsApiConfig;
import com.polycom.hst.wechat.model.WechatJsTicket;
import com.polycom.hst.wechat.model.WechatMpMenu;
import com.polycom.hst.wechat.model.WechatMpToken;
import com.polycom.hst.wechat.service.WechatConfigService;
import com.polycom.hst.wechat.service.WechatMpAccessService;
import com.polycom.hst.wechat.utils.HttpKit;
import com.polycom.hst.wechat.utils.WechatUtils;

@Service
public class WechatMpAccessServiceImpl implements WechatMpAccessService {
	private static final Logger log = LoggerFactory.getLogger(WechatMpAccessServiceImpl.class);
	private volatile String accessToken = "";
	private volatile String jsapiTicket = "";
	private boolean isNeedJsSdk = true;
	@Autowired
	private WechatConfigService wechatConfigService;

	@Override
	public boolean getAccessToken() {
		if (wechatConfigService != null) {
			String url = String.format(WechatUrl.GET_MP_TOKEN_URL,
					wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_APPID),
					wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_APPSECRET));
			try {
				log.info("Getting global access token from ({})", url);
				String tokenStr = HttpKit.get(url);
				log.info("response for getting global access token: ({})", tokenStr);
				WechatMpToken result = new Gson().fromJson(tokenStr, WechatMpToken.class);
				if (!Strings.isNullOrEmpty(result.getAccess_token())) {
					accessToken = result.getAccess_token();
					if (isNeedJsSdk)
						getJsApiTicket(accessToken);
					return true;
				}

			} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
				log.error("Exception when getting global access token by code: {}", e);
			}
		}

		return false;
	}

	@Override
	public boolean createMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WechatMpMenu queryMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WechatJsApiConfig getJsApiConfig(StringBuffer url, ServletRequest request) {
		// Reset query order of URL to compatible with Wechat.
		WechatJsApiConfig config;
		url.append("?");
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, String[]> map = Maps.newTreeMap();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			map.put(paramName, paramValues);
		}

		for (String key : map.keySet()) {
			String[] paramValues = map.get(key);
			for (String paramValue : paramValues) {
				url.append(key).append("=").append(paramValue).append("&");
			}
		}

		// Get JS-SDK authentication configuration
		try {
			config = WechatUtils.getJsApiSignature(jsapiTicket, url.substring(0, url.length() - 1));
			if (wechatConfigService != null)
				config.setAppId(wechatConfigService.getWechatConfigByKey(WechatConfigKey.WECHAT_APPID));
		} catch (Exception e) {
			config = new WechatJsApiConfig();
			e.printStackTrace();
		}

		return config;
	}

	private void getJsApiTicket(String accessToken) {
		if (wechatConfigService != null) {
			String url = String.format(WechatUrl.GET_JSSDK_TOKEN_URL, accessToken);

			try {
				log.info("Getting JS SDK ticket from ({})", url);
				String tikcetStr = HttpKit.get(url);
				log.info("response for JS SDK ticke: ({})", tikcetStr);
				WechatJsTicket result = new Gson().fromJson(tikcetStr, WechatJsTicket.class);
				if (!Strings.isNullOrEmpty(result.getTicket()))
					jsapiTicket = result.getTicket();
			} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
				log.error("Exception when getting global access token by code: {}", e);
			}
		}
	}

	@PreDestroy
	private void destory() {
		if (wechatConfigService != null)
			wechatConfigService = null;
	}
}

package com.polycom.hst.wechat.service.impl;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.polycom.hst.wechat.service.WechatConfigService;

@Service
public class WechatConfigServiceImpl implements WechatConfigService {
	private Properties pros = null;

	@Override
	public String getWechatConfigByKey(String key) {
		return pros == null || pros.getProperty(key) == null ? "" : pros.getProperty(key);
	}
	
	@PostConstruct
	private void delcare() {
		try {
			pros = new Properties();
			pros.load(this.getClass().getResourceAsStream("/wechat-config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PreDestroy
	private void destory() {
		if(pros != null)
			pros = null;
	}

}

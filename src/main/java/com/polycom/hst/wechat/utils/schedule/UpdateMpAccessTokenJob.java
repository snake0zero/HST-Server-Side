package com.polycom.hst.wechat.utils.schedule;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.polycom.hst.wechat.service.WechatMpAccessService;

public class UpdateMpAccessTokenJob {
	private static final Logger log = LoggerFactory.getLogger(UpdateMpAccessTokenJob.class);
	private volatile boolean isExcuting = false;
	private long NO_ACCESS_TOKEN_TIMEOUT = 5 * 1000L;
	@Autowired
	private WechatMpAccessService wechatMpAccessService;

	public void execute() {
		if (!isExcuting) {
			isExcuting = true;
			while (true) {
				log.info("Update access token at {}", (new Date()).toString());
				if (wechatMpAccessService != null && wechatMpAccessService.getAccessToken()) {
					break;
				} else {
					try {
						Thread.sleep(NO_ACCESS_TOKEN_TIMEOUT);
					} catch (InterruptedException e) {
						log.info("The thread that is getting access token is interrupted.");
						e.printStackTrace();
						break;
					}
				}
			}
			isExcuting = false;
		}
	}

	@PostConstruct
	private void delcare() {
	}

	@PreDestroy
	private void destory() {
		if (wechatMpAccessService != null)
			wechatMpAccessService = null;
	}
}

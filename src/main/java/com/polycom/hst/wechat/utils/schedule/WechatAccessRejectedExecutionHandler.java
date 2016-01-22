package com.polycom.hst.wechat.utils.schedule;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WechatAccessRejectedExecutionHandler implements RejectedExecutionHandler {
	private static final Logger log = LoggerFactory.getLogger(WechatAccessRejectedExecutionHandler.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		log.info("Task {} rejected from {}", r.toString(), executor.toString());
		log.info("Will re-execute it in the caller's thread");
		if (!executor.isShutdown()) {
			log.info("Run the exception task.");
			r.run();
		}
	}

}

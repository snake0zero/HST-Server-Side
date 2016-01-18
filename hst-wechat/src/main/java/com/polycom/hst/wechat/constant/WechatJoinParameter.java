package com.polycom.hst.wechat.constant;

public enum WechatJoinParameter {
	signature("signature"), timestamp("timestamp"), nonce("nonce"), echostr("echostr");
	private String parameter;

	private WechatJoinParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public String toString() {
		return parameter;
	}
}

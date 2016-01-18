package com.polycom.hst.wechat.constant;

public enum WechatAuthType {
	base("base"), login("login"), qrcode("qrcode"), user("user");
	private String type;

	private WechatAuthType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}

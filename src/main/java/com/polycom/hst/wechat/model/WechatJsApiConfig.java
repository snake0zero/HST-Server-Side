package com.polycom.hst.wechat.model;

public class WechatJsApiConfig {
	private String appId;
	private String timestamp;
	private String nonceStr;
	private String signature;

	public WechatJsApiConfig() {

	}

	public WechatJsApiConfig(String appId, String timestamp, String nonceStr, String signature) {
		this.appId = appId;
		this.timestamp = timestamp;
		this.nonceStr = nonceStr;
		this.signature = signature;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}

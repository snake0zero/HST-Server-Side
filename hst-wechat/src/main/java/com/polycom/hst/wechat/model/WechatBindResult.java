package com.polycom.hst.wechat.model;

public class WechatBindResult {
	private String msUrl;
	private String token;
	private String loginName;
	private String domainName;
	private String userType;
	private String cookieDomain;
	private String contextPath;

	public WechatBindResult(String msUrl, String token, String loginName, String domainName, String userType,
			String cookieDomain, String contextPath) {
		this.msUrl = msUrl;
		this.token = token;
		this.loginName = loginName;
		this.domainName = domainName;
		this.userType = userType;
		this.cookieDomain = cookieDomain;
		this.contextPath = contextPath;
	}

	public WechatBindResult() {

	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getMsUrl() {
		return msUrl;
	}

	public void setMsUrl(String msUrl) {
		this.msUrl = msUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}

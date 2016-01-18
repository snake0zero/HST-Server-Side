package com.polycom.hst.wechat.model;

public class WechatPlcmLoginUser {
	private String loginName;
	private String password;
	//Default portal type(0) for branch
	//null for current http://ms.plcmchina.com/userportal/api/rest/users/login
	private Integer loginType = null;

	public WechatPlcmLoginUser(String loginName, String password) {
		this.loginName = loginName;
		this.password = password;
	}

	public WechatPlcmLoginUser() {

	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
}

package com.polycom.hst.wechat.model;

import java.util.List;

public class WechatPlcmRestUser {
	private String userId;
	private String fullName;
	private String role;
	private String status;
	private boolean isOnline;
	private String description;
	private String loginPassword;
	private String loginName;
	private long createTime;
	private long passwordUPTime;
	private String domainName;
	private String email;
	private String userType;
	private List<WechatPlcmRestGroup> groups;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getPasswordUPTime() {
		return passwordUPTime;
	}
	public void setPasswordUPTime(long passwordUPTime) {
		this.passwordUPTime = passwordUPTime;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public List<WechatPlcmRestGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<WechatPlcmRestGroup> groups) {
		this.groups = groups;
	}

}

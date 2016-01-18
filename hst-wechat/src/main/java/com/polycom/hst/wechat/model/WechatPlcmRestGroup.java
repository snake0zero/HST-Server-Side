package com.polycom.hst.wechat.model;

import java.util.List;

public class WechatPlcmRestGroup {
	private String groupId;
	private String groupType;
	private String groupName;
	private String domainName;
	private String description;
	private List<WechatPlcmRestUser> users;
	private List<String> parentGrps;
	private String email;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<WechatPlcmRestUser> getUsers() {
		return users;
	}
	public void setUsers(List<WechatPlcmRestUser> users) {
		this.users = users;
	}
	public List<String> getParentGrps() {
		return parentGrps;
	}
	public void setParentGrps(List<String> parentGrps) {
		this.parentGrps = parentGrps;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}

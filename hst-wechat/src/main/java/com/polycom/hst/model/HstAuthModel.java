package com.polycom.hst.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class HstAuthModel {
	private String openid;
	private String userid;

	public HstAuthModel() {

	}
	
	public HstAuthModel(String openid) {
		this.openid = openid;
	}

	public HstAuthModel(String openid, String userid) {
		this.openid = openid;
		this.userid = userid;
	}
	

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}

		HstAuthModel target = (HstAuthModel) obj;

		return new EqualsBuilder().append(getOpenid(), target.getOpenid()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getOpenid()).toHashCode();
	}

}

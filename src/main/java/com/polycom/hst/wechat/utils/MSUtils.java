package com.polycom.hst.wechat.utils;

import com.google.common.base.Strings;
import com.polycom.hst.wechat.model.WechatPlcmRestUser;

public class MSUtils {
	public static String getUserId(WechatPlcmRestUser user) {
		String userId = "";
		if (user != null) {
			if ("local".equals(user.getUserType().toLowerCase())) {
				userId = user.getLoginName();
			} else if(!Strings.isNullOrEmpty(user.getDomainName()) && !Strings.isNullOrEmpty(user.getLoginName())){
				userId = user.getDomainName() + "\\" + user.getLoginName();
			}
		}
		return Strings.isNullOrEmpty(userId) ? "" : userId;
	}
}

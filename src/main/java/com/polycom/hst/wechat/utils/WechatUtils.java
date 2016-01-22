package com.polycom.hst.wechat.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.polycom.hst.wechat.model.WechatJsApiConfig;

public class WechatUtils {
	private static final Logger log = LoggerFactory.getLogger(WechatUtils.class);
	private static final String SHA_ONE = "SHA-1";
	private static final String ENCODE_UTF8 = "UTF-8";
	private static final String JSAPI_NONCESTR = "noncestr";
	private static final String JSAPI_TICKET = "jsapi_ticket";
	private static final String JSAPI_TIMESTAMP = "timestamp";
	private static final String JSAPI_URL = "url";
	
	public static final boolean checkSignature(String token, String signature, String timestamp, String nonce) {
		// 1. 将token、timestamp、nonce三个参数进行字典序排庄
		String[] validatingArray = { token, timestamp, nonce };
		Arrays.sort(validatingArray);
		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		StringBuffer sb = new StringBuffer();
		for (String str : validatingArray) {
			sb.append(str);
		}
		MessageDigest mdSha1 = null;
		try {
			mdSha1 = MessageDigest.getInstance(SHA_ONE);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mdSha1.update(sb.toString().getBytes());
		byte[] codedBytes = mdSha1.digest();
		String codedString = new BigInteger(1, codedBytes).toString(16);
		// 3. 获得加密后的字符串可与signature对比，标识该请求来源于微信
		if (codedString.equals(signature))
			return true;

		return false;
	}

	public static final WechatJsApiConfig getJsApiSignature(String ticket, String url) throws Exception {
		if (!Strings.isNullOrEmpty(ticket) && !Strings.isNullOrEmpty(url)) {
			String noncestr = createNoncestr();
			String timestamp = createTimestamp();
			Map<String, String> map = Maps.newHashMap();
			map.put(JSAPI_NONCESTR, noncestr);
			map.put(JSAPI_TICKET, ticket);
			map.put(JSAPI_TIMESTAMP, timestamp);
			map.put(JSAPI_URL, url);
			return new WechatJsApiConfig(null, timestamp, noncestr, getSignature(map));
		}
		return new WechatJsApiConfig();
	}
	
	private static final String createNoncestr() {
		return UUID.randomUUID().toString();
	}

	private static final String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	private static final String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	private static String getSignature(Map<String, String> map)
			throws Exception {
		SortedMap<String, String> smap = Maps.newTreeMap(
				new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
		smap.putAll(map);
		StringBuilder prepareStr = new StringBuilder();
		for (String key : smap.keySet()) {
			prepareStr.append(key).append("=").append(smap.get(key))
					.append("&");
		}
		if (prepareStr.length() > 0)
			prepareStr.deleteCharAt(prepareStr.length() - 1);
		log.info("JS-API Prepared signature: ({})", prepareStr.toString());
		MessageDigest crypt = MessageDigest.getInstance(SHA_ONE);
		crypt.reset();
		crypt.update(prepareStr.toString().getBytes(ENCODE_UTF8));
		return byteToHex(crypt.digest());
	}
}

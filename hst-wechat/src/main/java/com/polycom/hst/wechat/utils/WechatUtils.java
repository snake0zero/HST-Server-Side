package com.polycom.hst.wechat.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class WechatUtils {
	public static final boolean checkSignature(String token,String signature,String timestamp,String nonce){
		// 1. 将token、timestamp、nonce三个参数进行字典序排庄
		String[] validatingArray = { token, timestamp, nonce };
		Arrays.sort(validatingArray);
		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		StringBuffer sb = new StringBuffer();  
	    for (String str : validatingArray)  
	    {  
	        sb.append(str);  
	    }  
	    MessageDigest mdSha1 = null;  
	    try  
	    {  
	        mdSha1 = MessageDigest.getInstance("SHA-1");  
	    }  
	    catch (NoSuchAlgorithmException e)  
	    {  
	        e.printStackTrace();  
	    }  
	    mdSha1.update(sb.toString().getBytes());  
	    byte[] codedBytes = mdSha1.digest();  
	    String codedString = new BigInteger(1, codedBytes).toString(16);  
	    // 3. 获得加密后的字符串可与signature对比，标识该请求来源于微信
	    if (codedString.equals(signature)) return true;

	    return false;
	}
}

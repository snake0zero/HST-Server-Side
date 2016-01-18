package com.polycom.hst.wechat.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class TestSuite {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String one = "http://www.qq.com?xxx=yy&yy=zz";
		one += "&red=" + URLEncoder.encode("http://www.qq.com?xxx=yy&yy=zz", "UTF-8");
		System.out.println(one);
		String two = URLEncoder.encode(one,"UTF-8");
		System.out.println(two);
		System.out.println(URLDecoder.decode(two, "UTF-8"));
		System.out.println(URLDecoder.decode(URLDecoder.decode(two, "UTF-8"),"UTF-8"));
		String three = "http://10.220.207.214/userportal/index.html#/player/vod/Uf8b16fdb7a4f42bf95b223ad33e2b31e";
		System.out.println(URLEncoder.encode(three,"UTF-8"));
		System.out.println(URLDecoder.decode("http%3A%2F%2Flocalhost%3A8080%2Fhst-wechat%2Fauthms%2Fmsurl%3Dhttp%253A%2F%2F10.220.207.214%2Fuserportal%2Findex.html%2523%2Fplayer%2Fvod%2FUf8b16fdb7a4f42bf95b223ad33e2b31e", "UTF-8"));
		System.out.println(URLEncoder.encode("http://10.220.207.214/userportal/index.html#/mediacenter/mymedia/medias/1/Time/","UTF-8"));
		
	}

}

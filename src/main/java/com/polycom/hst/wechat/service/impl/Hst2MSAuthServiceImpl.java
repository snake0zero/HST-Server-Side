package com.polycom.hst.wechat.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.polycom.hst.model.HstAuthModel;
import com.polycom.hst.wechat.constant.MsConfigKey;
import com.polycom.hst.wechat.encryption.AES;
import com.polycom.hst.wechat.model.WechatPlcmLoginResult;
import com.polycom.hst.wechat.model.WechatPlcmLoginUser;
import com.polycom.hst.wechat.service.Hst2MSAuthService;
import com.polycom.hst.wechat.service.WechatConfigService;
import com.polycom.hst.wechat.utils.HttpKit;
import com.polycom.hst.wechat.utils.MSUtils;

import static com.polycom.hst.wechat.constant.WechatConstant.*;

@Service
public class Hst2MSAuthServiceImpl implements Hst2MSAuthService {
	private static final Logger log = LoggerFactory.getLogger(Hst2MSAuthServiceImpl.class);
	private CopyOnWriteArrayList<HstAuthModel> repos;
	private File targetFile;
	private Pattern pattern = Pattern.compile("-?\\d+");
	@Autowired
	private WechatConfigService wechatConfigService;

	@Override
	public boolean isAuthByOpenid(String openid) {
		return getModelByOpenid(openid) != null;
	}

	@Override
	public boolean saveAuthInfo(String openid, String userid) {
		if(repos.addIfAbsent(new HstAuthModel(openid, userid)))
			return writeRepository();
		else 
			return false;
	}

	@Override
	public String ssoLogin(String openid, String msUrl) {
		HstAuthModel existAccount = getModelByOpenid(openid);
		
		if(existAccount != null)
			return doSSOLogin(existAccount.getUserid(), msUrl);
		
		return "";
	}
	
	@Override
	public WechatPlcmLoginResult login(String openid, String account, String password) {
		WechatPlcmLoginResult result = doLogin(account, password);
		// bind wechat and ms account after login successfully.
		if(!Strings.isNullOrEmpty(result.getToken()))
			saveAuthInfo(openid, MSUtils.getUserId(result.getUser()));

		return result;
	}

	@Override
	public boolean deleteAuthByOpenid(String openid) {
		if (repos.remove(new HstAuthModel(openid)))
			return writeRepository();
		else
			return false;
	}

	@PostConstruct
	private void delcare() {
		String jsonStr = readRepository();
		if (!Strings.isNullOrEmpty(jsonStr)) {
			Gson gson = new Gson();
			Type listType = new TypeToken<CopyOnWriteArrayList<HstAuthModel>>() {
			}.getType();
			repos = gson.fromJson(jsonStr, listType);
		}
		if (repos == null)
			repos = Lists.newCopyOnWriteArrayList();
	}

	@PreDestroy
	private void destory() {
		if (repos != null)
			repos = null;
		if (wechatConfigService != null)
			wechatConfigService = null;
	}
	
	private String doSSOLogin(String userid, String msUrl){
		String ssoUrl = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_SSO_URL);
		String contextPath = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_CONTEXT_PATH);
		String domain = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_DOMAIN);
		String scheme = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_SCHEME);
		String ssoId = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_SSO_ID);
		String ssoPwd = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_SSO_PWD);
		String encrytTgt = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_ENCRYT_TARGET);
		String ssoAseKey = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_SSO_AES_KEY);
		String ssoUserId = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_SSO_USER_ID);
		
		encrytTgt = String.format(encrytTgt, ssoUserId, userid, ssoId, ssoPwd, msUrl);
		
		return String.format(scheme + "://" + domain + contextPath + ssoUrl, AES.encrypt(encrytTgt, ssoAseKey));
	}
	
	private WechatPlcmLoginResult doLogin(String account, String password){
		WechatPlcmLoginResult result = new WechatPlcmLoginResult();
		try {
			if (wechatConfigService != null) {
				String loginUrl = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_LOGIN_URL);
				String contextPath = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_CONTEXT_PATH);
				String domain = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_DOMAIN);
				String scheme = wechatConfigService.getWechatConfigByKey(MsConfigKey.MS_SCHEME);
				
				String params = getJsonStr(new WechatPlcmLoginUser(account, password));
				
				String postResult = HttpKit.post(scheme + "://" + domain + contextPath + loginUrl, params,
						getRequestHeader());
				
				log.info("The result from Media suite is: ({}) while binding wechat.", postResult);
				
				result = getJsonObj(postResult, WechatPlcmLoginResult.class);
				result.setContextPath(contextPath);
				result.setCookieDomain(domain);
			}

		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			log.error("Exception from Media suite: ({})", e);
			e.printStackTrace();
			if (!Strings.isNullOrEmpty(e.getMessage())) {
				Matcher m = pattern.matcher(e.getMessage());
				while (m.find()) {
					result.setErrorCode(m.group());
				}
			}
		}
		return result;
	}

	private String readRepository() {
		BufferedReader in = null;
		String tmpFolder = System.getProperty("java.io.tmpdir");
		tmpFolder = Strings.isNullOrEmpty(tmpFolder) ? System.getProperty("catalina.base") + File.separator + "temp" : tmpFolder;
		tmpFolder = tmpFolder + File.separator + "auth.json";
		log.info("temp HST Storage is: ({})", tmpFolder);
		try {
			//in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/auth.json"), UTF8));
			targetFile = new File(tmpFolder);
			if(!targetFile.isFile())
				targetFile.createNewFile();
			
			in = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), UTF8));
			StringBuilder sb = new StringBuilder();
			int r;
			while ((r = in.read()) != -1) {
				char ch = (char) r;
				sb.append(ch);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return "[]";
	}

	private HstAuthModel getModelByOpenid(final String openid) {
		//Below JDK 1.7
		/*Collection<HstAuthModel> result = Collections2.filter(repos, new Predicate<HstAuthModel>() {
			@Override
			public boolean apply(HstAuthModel input) {
				return openid.equals(input.getOpenid());
			}
		});*/
		
		Collection<HstAuthModel> result = Collections2.filter(repos, input -> openid.equals(input.getOpenid()));
		return result == null || result.size() == 0 ? null : Lists.newLinkedList(result).get(0);
	}
	
	private boolean writeRepository() {
		BufferedWriter out = null;
		try {
			//File file = new File(this.getClass().getResource("/auth.json").toURI());
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), UTF8));
			out.write(getJsonListStr(repos));
			out.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return false;
	}
	
	private <E> String getJsonListStr(List<E> list) {
		Type listType = new TypeToken<List<E>>() {
		}.getType();
		return (new Gson()).toJson(list, listType);
	}
	
	private <E> String getJsonStr(E target) {
		return (new Gson()).toJson(target);
	}
	
	private <E> E getJsonObj(String target, Class<E> clazz) {
		return (new Gson()).fromJson(target, clazz);
	}
	
	private Map<String, String> getRequestHeader(){
		Map<String, String> headers = Maps.newHashMap();
		headers.put("Content-Type", MsConfigKey.LOGIN_USER_JSON_TYPE);
		headers.put("Accept", MsConfigKey.LOGIN_RESULT_JSON_TYPE);
		return headers;
	}
}

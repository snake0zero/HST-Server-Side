package com.polycom.hst.wechat.utils;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;

public class HttpKit {
	private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String _GET  = "GET";
    private static final String _POST = "POST";
    private static final String LINE_BREAK = System.getProperty("line.separator");
    
    /**
     * Initialize http connection
     */
    private static HttpURLConnection initHttp (String url, String method, Map<String, String> headers) throws IOException {
        URL _url = new URL(url);
        HttpURLConnection http = (HttpURLConnection) _url.openConnection();
        // connect timeout
        http.setConnectTimeout(25000);
        // read timeout
        http.setReadTimeout(25000);
        http.setRequestMethod(method);
        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (null != headers && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        http.setDoOutput(true);
        http.setDoInput(true);
        http.connect();
        return http;
    }
    
    /**
     * Initialize https connection
     */
    private static HttpsURLConnection initHttps (String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        TrustManager[] tm = { new MyX509TrustManager() };  
        //System.setProperty("https.protocols", "SSLv3");
        System.setProperty("https.protocols", "TLSv1");
        SSLContext sslContext = SSLContext.getInstance("TLS", "SunJSSE");  
        sslContext.init(null, tm, new java.security.SecureRandom());  

        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL _url = new URL(url);
        HttpsURLConnection http = (HttpsURLConnection) _url.openConnection();
        // Verify host name
        http.setHostnameVerifier(new HttpKit().new TrustAnyHostnameVerifier());
        // connect timeout
        http.setConnectTimeout(25000);
        // read timeout
        http.setReadTimeout(25000);
        http.setRequestMethod(method);
        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (null != headers && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        http.setSSLSocketFactory(ssf);
        http.setDoOutput(true);
        http.setDoInput(true);
        http.connect();
        return http;
    }

    /**
     * Execute get request
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException {
        HttpURLConnection http = null;
        if (isHttps(url)) {
            http = initHttps(initParams(url, params), _GET, headers);
        } else {
            http = initHttp(initParams(url, params), _GET, headers);
        }
        InputStream in = http.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
        String valueString = null;
        StringBuffer bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null){
            bufferRes.append(valueString);
        }
        in.close();
        if (http != null) {
            http.disconnect();
        }
        return bufferRes.toString();
    }
    
    /**
     * Execute get request without parameter
     */
    public static String get(String url) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException {
        return get(url, null);
    }
    
    /**
     * Execute get request with parameter
     */
    public static String get(String url, Map<String, String> params) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException {
        return get(url, params, null);
    }
    
    /**
     * Execute post request
     */
    public static String post(String url, String params) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
    	return post(url, params, null);
    }
    
    /**
     * Execute post request
     */
    public static String post(String url, String params, Map<String, String> headers) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        HttpURLConnection http = null;
        if (isHttps(url)) {
            http = initHttps(url, _POST, headers);
        } else {
            http = initHttp(url, _POST, headers);
        }
        OutputStream out = http.getOutputStream();
        out.write(params.getBytes(DEFAULT_CHARSET));
        out.flush();
        out.close();

        InputStream in = http.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
        String valueString = null;
        StringBuffer bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null){
            bufferRes.append(valueString);
        }
        in.close();
        if (http != null) {
            http.disconnect();
        }
        return bufferRes.toString();
    }
    
    /**
     * Upload media file
     */
    public static String upload(String url,File file) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL"; // define data boundary
        StringBuffer bufferRes = null;
        URL urlGet = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
        conn.setUseCaches(false);  
        conn.setRequestMethod("POST");  
        conn.setRequestProperty("connection", "Keep-Alive");  
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");  
        conn.setRequestProperty("Charsert", "UTF-8");   
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
          
        OutputStream out = new DataOutputStream(conn.getOutputStream());  
        byte[] end_data = (LINE_BREAK + "--" + BOUNDARY + "--" + LINE_BREAK).getBytes();// define data boundary
        StringBuilder sb = new StringBuilder();    
        sb.append("--");    
        sb.append(BOUNDARY);    
        sb.append(LINE_BREAK);    
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\""+ file.getName() + "\"" + LINE_BREAK);    
        sb.append("Content-Type:application/octet-stream" + LINE_BREAK + LINE_BREAK);    
        byte[] data = sb.toString().getBytes();  
        out.write(data);  
        DataInputStream fs = new DataInputStream(new FileInputStream(file));  
        int bytes = 0;  
        byte[] bufferOut = new byte[1024];  
        while ((bytes = fs.read(bufferOut)) != -1) {  
            out.write(bufferOut, 0, bytes);  
        }  
        out.write(LINE_BREAK.getBytes());
        fs.close();  
        out.write(end_data);  
        out.flush();    
        out.close();   

        InputStream in = conn.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
        String valueString = null;
        bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null){
            bufferRes.append(valueString);
        }
        in.close();
        if (conn != null) {
            conn.disconnect();
        }
        return bufferRes.toString();
    }    
   
    /**
     * Initialize parameter
     */
    public static String initParams(String url, Map<String, String> params) throws UnsupportedEncodingException {
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        }
        sb.append(map2Url(params));
        return sb.toString();
    }
    
    /**
     * Convert map to query string
     */
    public static String map2Url(Map<String, String> paramToMap) throws UnsupportedEncodingException {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value)) {
                    url.append(URLEncoder.encode(value, DEFAULT_CHARSET));
            }
        }
        return url.toString();
    }
    
    /**
	 * Add cookie
	 */
	public static void addCookie(HttpServletRequest req, HttpServletResponse resp, String domain, String path, String key,
			String cookieStr) {
		Cookie cookie = new Cookie(key, cookieStr);
		
		if(!Strings.isNullOrEmpty(domain))
			cookie.setDomain(domain);
		
		if(!Strings.isNullOrEmpty(path))
			cookie.setPath(path);
		
		cookie.setHttpOnly(true);
		
		//cookie.setMaxAge(Integer.MAX_VALUE);
		
		resp.addCookie(cookie);
	}
	
    /**
	 * Add URL Query
     * @throws URISyntaxException 
	 */
	public static URI appendQuery(String uri, String appendQuery) throws URISyntaxException {
		URI oldUri = new URI(uri);

		String newQuery = oldUri.getQuery();
		if (newQuery == null) {
			newQuery = appendQuery;
		} else {
			newQuery += "&" + appendQuery;
		}
		URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());
		return newUri;
	}
    
    /**
     * Verify request scheme
     */
    private static boolean isHttps (String url) {
        return url.startsWith("https");
    }
    
    /**
     * Host name verifier
     */
    public class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}

class MyX509TrustManager implements X509TrustManager {

    public X509Certificate[] getAcceptedIssuers() {
        return null;  
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
}

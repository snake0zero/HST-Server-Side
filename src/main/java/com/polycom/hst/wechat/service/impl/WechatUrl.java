package com.polycom.hst.wechat.service.impl;

class WechatUrl {
	static String AUTH_BASE_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=wx#wechat_redirect";
	static String AUTH_USER_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=wx#wechat_redirect";
	static String AUTH_LOGIN_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=wx#wechat_redirect";
	static String AUTH_QRCODE_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=wx#wechat_redirect";
    static String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    static String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
    static String DEL_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s";
    static String GET_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    static String REFRESH_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    static String GET_MP_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    static String GET_JSSDK_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=%s";
    static String SEND_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
    static String ADD_HD_URL = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=%s";
    static String UPT_HD_URL = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=%s";
    static String DEL_HD_URL = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=%s";
    static String CONFIG_HD_PHOTO_URL = "http://api.weixin.qq.com/customservice/kfaccount/uploadheadimg?access_token=%s&kf_account=%s";
    static String GET_ALL_HD_URL = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=%s";
    static String CHECK_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";
    static String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    //Send Message Url
    
    //Maintain Media Url
}

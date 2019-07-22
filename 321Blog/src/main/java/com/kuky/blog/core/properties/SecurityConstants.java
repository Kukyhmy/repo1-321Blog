/**
 * 
 */
package com.kuky.blog.core.properties;


public interface SecurityConstants {
	
	/**
	 * 当请求需要身份认证时，默认跳转的url
	 * 
	 */
	public static final String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";
	/**
	 * 默认的用户名密码登录请求处理url
	 */
	public static final String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";
	/**
	 * 默认登录页面
	 * 
	 */
	public static final String DEFAULT_LOGIN_PAGE_URL = "/login.html";
	//public static final String DEFAULT_LOGIN_PAGE_URL = "/imooc-signIn.html";
	/**
	 * session失效默认的跳转地址
	 */
	public static final String DEFAULT_SESSION_INVALID_URL = "/session/invalid";

	/**
	 * weibo appID
	 */
	String DEFAULT_SOCIAL_WEIBO_APP_ID = "491608476";

	/**
	 * weibo appsECRET
	 */
	String DEFAULT_SOCIAL_WEIBO_APP_SECRET = "b2dd0d4199ddc9d64e75d9dd6007ca82";
}

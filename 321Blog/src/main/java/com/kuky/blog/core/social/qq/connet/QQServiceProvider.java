/**
 * 
 */
package com.kuky.blog.core.social.qq.connet;

import com.kuky.blog.core.social.qq.api.QQ;
import com.kuky.blog.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;



public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

	private String appId;
	
	/**
	 * 1.将用户导向认证服务器的URL
	 */
	private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
	
	/**
	 * 4.向认证服务器 申请令牌 的URL
	 */
	private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";
	
	
	
	public QQServiceProvider(String appId, String appSecret) {
		super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
		this.appId = appId;
	}
	
	@Override
	public QQ getApi(String accessToken) {
		return new QQImpl(accessToken, appId);
	}

}

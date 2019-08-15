/**
 * 
 */
package com.kuky.blog.core.social.weixin.config;

import com.kuky.blog.core.properties.SecurityProperties;
import com.kuky.blog.core.properties.WeixinProperties;
import com.kuky.blog.core.social.BlogConnectView;
import com.kuky.blog.core.social.weixin.connect.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;


/**
 * 微信登录配置
 * 
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "321blog.security.social.weixin", name = "app-id")
public class WeixinAutoConfiguration extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();
		return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
				weixinConfig.getAppSecret());
	}
	/**
	 * /connect/weixin POST请求,绑定微信返回connect/weixinConnected视图
	 * /connect/weixin DELETE请求,解绑返回connect/weixinConnect视图
	 * @return
	 */
	@Bean({"connect/weixinConnect", "connect/weixinConnected"})
	@ConditionalOnMissingBean(name = "weixinConnectedView")
	public View weixinConnectedView() {
		return new BlogConnectView();
	}
	
}

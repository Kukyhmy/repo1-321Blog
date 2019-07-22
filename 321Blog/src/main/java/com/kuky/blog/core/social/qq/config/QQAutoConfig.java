/**
 * 
 */
package com.kuky.blog.core.social.qq.config;

import com.kuky.blog.core.properties.QQProperties;
import com.kuky.blog.core.properties.SecurityProperties;
import com.kuky.blog.core.social.BlogConnectView;
import com.kuky.blog.core.social.qq.connet.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;



@Configuration
@ConditionalOnProperty(prefix = "blog.security.social.qq", name = "app-id")
@Order(2)
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;


	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qqConfig = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
	}

	/**
	 *  /connect/qq POST请求,绑定微信返回connect/qqConnected视图
	 *  /connect/qq DELETE请求,解绑返回connect/qqConnect视图
	 * @return
	 */
	@Bean({"connect/qqConnect", "connect/qqConnected"})
	@ConditionalOnMissingBean(name = "qqConnectedView")
	public View qqConnectedView() {
		return new BlogConnectView();
	}
}

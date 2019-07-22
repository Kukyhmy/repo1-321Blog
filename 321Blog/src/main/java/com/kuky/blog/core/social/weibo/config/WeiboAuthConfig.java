package com.kuky.blog.core.social.weibo.config;

import com.kuky.blog.core.properties.SecurityConstants;
import com.kuky.blog.core.social.BlogConnectView;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.weibo.connect.WeiboConnectionFactory;
import org.springframework.web.servlet.View;


@Configuration
public class WeiboAuthConfig extends SocialAutoConfigurerAdapter {

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new WeiboConnectionFactory(SecurityConstants.DEFAULT_SOCIAL_WEIBO_APP_ID, SecurityConstants.DEFAULT_SOCIAL_WEIBO_APP_SECRET);
    }

    /**
     * /connect/qq POST请求,绑定微信返回connect/weixinConnected视图
     * /connect/qq DELETE请求,解绑返回connect/weixinConnect视图
     * @return
     */
    @Bean({"connect/weiboConnect", "connect/weiboConnected"})
    @ConditionalOnMissingBean(name = "weiboConnectedView")
    public View qqConnectedView() {
        return new BlogConnectView();
    }
}

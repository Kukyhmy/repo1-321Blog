package com.kuky.blog.basics.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author Kuky
 * @create 2019/7/25 19:34
 */
@Configuration
public class MySecurityMessages {

    /** 注册bean */
    @Bean
    public MessageSource messageSource() {
        Locale.setDefault(Locale.CHINA);
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        /**
         *	下面为加载自定义消息的properties文件，放在了resources下，里面的提示消息可以自己定义，
         *	比如：密码错误是，原文件中提示的是，坏的凭证，可以找到它对应的key，修改它的值为 用户名或密码错误。
         */
        messageSource.addBasenames("classpath:messages_zh_CN");

        return messageSource;
    }

}

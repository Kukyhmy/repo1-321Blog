package com.kuky.blog.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuky
 * @create 2019/7/6 15:25
 */
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource dataSource(){
        return new DruidDataSource();
    }


    //配置对web整个应用的监控
    @Bean
    public ServletRegistrationBean getViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet());
        String[] urlArr = new String[]{"/druid/*"};
        bean.setUrlMappings(Arrays.asList(urlArr)); //设置对本servlet的映射地址
        //还需要设置druid后台页面的一些基本东西，后台的登录用户名和密码，后台有哪些ip地址可以访问，哪些不可以
        Map<String,String> initParams = new HashMap<>();
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","123456");
        initParams.put("allow",""); //设置为空字符串就表示任何ip地址都可以访问druid的监控后台
        //initParams.put("deny","10.33.12.1");
        bean.setInitParameters(initParams);
        return  bean;
    }

    //配置过滤器来实现对web应用的所有访问请求进行过滤，从而实现对web应用对数据库的访问监控
    @Bean
    public FilterRegistrationBean getFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        bean.setUrlPatterns(Arrays.asList(new String[]{"/*"}));
        //不能对所有的请求都拦截，静态、druid后台  这些请求需要排除到拦截范围之外
        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,*.jpg,*png,/druid/*");
        bean.setInitParameters(initParams);
        return bean;

    }
}

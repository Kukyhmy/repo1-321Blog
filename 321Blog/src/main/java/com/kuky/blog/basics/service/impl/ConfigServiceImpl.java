package com.kuky.blog.basics.service.impl;

import com.kuky.blog.basics.dao.BlogConfigMapper;
import com.kuky.blog.basics.entity.BlogConfig;
import com.kuky.blog.basics.service.ConfigService;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kuky
 * @create 2019/7/22 0:36
 */
@Repository
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private BlogConfigMapper blogConfigMapper;


    public static final String websiteName = "321 321blog";
    public static final String websiteDescription = "321 blog是SpringBoot2+Thymeleaf+Mybatis建造的个人博客网站.SpringBoot实战博客";
    public static final String websiteLogo = "/user/dist/img/logo2.png";
    public static final String websiteIcon = "/user/dist/img/favicon.png";

    public static final String yourAvatar = "/user/dist/img/kuky.png";
    public static final String yourEmail = "739663514@qq.com";
    public static final String yourName = "Kuky";

    public static final String footerAbout = "your personal 321blog. have fun.";
    public static final String footerICP = "浙ICP备 xxxxxx-x号";
    public static final String footerCopyRight = "@2019 十三";
    public static final String footerPoweredBy = "321 321blog";
    public static final String footerPoweredByURL = "##";

    /**
     *
     * @return
     */
    @Override
    public Map<String, String> getAllConfigs() {
        //获取所有的map并封装为map
        List<BlogConfig> blogConfigs = blogConfigMapper.selectAll();
        Map<String, String> configMap = blogConfigs.stream().collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));
        for (Map.Entry<String, String> config : configMap.entrySet()) {
            if ("websiteName".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteName);
            }
            if ("websiteDescription".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteDescription);
            }
            if ("websiteLogo".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteLogo);
            }
            if ("websiteIcon".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteIcon);
            }
            if ("yourAvatar".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(yourAvatar);
            }
            if ("yourEmail".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(yourEmail);
            }
            if ("yourName".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(yourName);
            }
            if ("footerAbout".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerAbout);
            }
            if ("footerICP".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerICP);
            }
            if ("footerCopyRight".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerCopyRight);
            }
            if ("footerPoweredBy".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerPoweredBy);
            }
            if ("footerPoweredByURL".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerPoweredByURL);
            }
        }
        return configMap;
    }
}

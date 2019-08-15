package com.kuky.blog.basics.service;

import java.util.Map;

/**
 * @author Kuky
 * @create 2019/7/22 0:36
 */
public interface ConfigService {
    /**
     * 获取所有的配置项
     *
     * @return
     */
    Map<String,String> getAllConfigs();

    /**
     * 修改配置项
     *
     * @param configName
     * @param configValue
     * @return
     */
    int updateConfig(String configName, String configValue);
}

package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/22 20:16
 */
@Repository
public interface BlogConfigMapper {


    List<BlogConfig> selectAll();


    BlogConfig selectByPrimaryKey(String configName);

    int updateByPrimaryKeySelective(BlogConfig record);
}

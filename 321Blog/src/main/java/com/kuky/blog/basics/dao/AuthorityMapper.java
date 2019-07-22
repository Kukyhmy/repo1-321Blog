package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.Authority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author Kuky
 * @create 2019/7/20 19:59
 */
@Component
public interface AuthorityMapper {

    Authority getAuthorityById(@Param("id") Long id);
}

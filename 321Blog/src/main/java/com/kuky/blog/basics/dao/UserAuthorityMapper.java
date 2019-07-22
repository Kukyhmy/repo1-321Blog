package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.Authority;
import com.kuky.blog.basics.entity.UserAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/21 9:52
 */
@Component
public interface UserAuthorityMapper {

    public List<Authority> selectUserWithAuthoritys(Long id);

    int insertSelective(UserAuthority ua);
}

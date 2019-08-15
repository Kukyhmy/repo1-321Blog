package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.Authority;
import com.kuky.blog.basics.entity.UserAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Kuky
 * @create 2019/7/21 9:52
 */
@Component
public interface UserAuthorityMapper {

    public List<Authority> selectUserWithAuthoritys(Long id);

    int insertSelective(UserAuthority ua);


    boolean addAuthority(Map<String,Map<Long, Integer>> params);

    List<UserAuthority> checkAuthority( Map<String,Map<Long, Integer>> params);

    boolean delete(@Param("userId") Long id, @Param("authorityId") Long authorityId);
}

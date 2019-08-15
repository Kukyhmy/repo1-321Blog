package com.kuky.blog.basics.service;

import com.kuky.blog.basics.entity.Authority;

import java.util.Map;

/**
 * @author Kuky
 * @create 2019/7/20 19:57
 */
public interface AuthorityService {

    Authority getAuthorityById(Long roleUserAuthorityId);


    boolean addAuthority(Map<Long, Integer> map);

    boolean deleteAuthority(Long id, Long i);
}

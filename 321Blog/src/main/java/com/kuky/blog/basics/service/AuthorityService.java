package com.kuky.blog.basics.service;

import com.kuky.blog.basics.entity.Authority;

/**
 * @author Kuky
 * @create 2019/7/20 19:57
 */
public interface AuthorityService {

    Authority getAuthorityById(Long roleUserAuthorityId);
}

package com.kuky.blog.basics.service.impl;

import com.kuky.blog.basics.dao.AuthorityMapper;
import com.kuky.blog.basics.entity.Authority;
import com.kuky.blog.basics.service.AuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Kuky
 * @create 2019/7/20 19:57
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Resource
    private AuthorityMapper authorityMapper;
    @Override
    public Authority getAuthorityById(Long roleUserAuthorityId) {
        return authorityMapper.getAuthorityById(roleUserAuthorityId);
    }
}

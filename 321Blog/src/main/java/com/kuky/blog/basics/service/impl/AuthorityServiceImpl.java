package com.kuky.blog.basics.service.impl;

import com.kuky.blog.basics.dao.AuthorityMapper;
import com.kuky.blog.basics.dao.UserAuthorityMapper;
import com.kuky.blog.basics.entity.Authority;
import com.kuky.blog.basics.entity.UserAuthority;
import com.kuky.blog.basics.service.AuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuky
 * @create 2019/7/20 19:57
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Resource
    private AuthorityMapper authorityMapper;

    @Resource
    private UserAuthorityMapper userAuthorityMapper;
    @Override
    public Authority getAuthorityById(Long roleUserAuthorityId) {
        return authorityMapper.getAuthorityById(roleUserAuthorityId);
    }


    @Override
    public boolean addAuthority(Map<Long, Integer> map) {
        Map<String, Map<Long, Integer>> params = new HashMap<>();
        params.put("keys", map); //注意，这里的"keys"，对应foreach中的collection

        List<UserAuthority> userAuthority = userAuthorityMapper.checkAuthority(params);
        if (userAuthority.isEmpty()) {
            return userAuthorityMapper.addAuthority(params); //这样把这个map的map传入到方法中才能进行插入
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteAuthority(Long id, Long authorityId) {
        return userAuthorityMapper.delete(id,authorityId);
    }
}


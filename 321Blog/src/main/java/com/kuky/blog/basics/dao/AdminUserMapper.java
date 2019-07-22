package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.AdminUser;
import org.springframework.stereotype.Component;

/**
 * @author Kuky
 * @create 2019/7/20 19:43
 */
@Component
public interface AdminUserMapper {
    
    int countByUsername(String data);

    int countByEmail(String data);


    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser findByUserName(String username);

    String findAuthorityByUserName(String username);
}

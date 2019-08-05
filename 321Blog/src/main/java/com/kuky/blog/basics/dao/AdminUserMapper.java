package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/20 19:43
 */
@Repository
public interface AdminUserMapper {


    int countByUsername(String data);

    int countByEmail(String data);


    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser findByUserName(String username);

    String findAuthorityByUserName(String username);

    int updateByPrimaryKeySelective(AdminUser record);

    List<AdminUser> listUsersByUsernames(@Param("usernamelist") List<String> usernamelist);

    AdminUser findByUserId(Long id);
}

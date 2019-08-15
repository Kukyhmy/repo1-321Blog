package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.entity.BlogCategory;
import com.kuky.blog.basics.utils.PageQueryUtil;
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

    List<AdminUser> findUserList(PageQueryUtil pageUtil);

    int getTotalUsers(PageQueryUtil pageUtil);

    /**
     * 登陆方法
     *
     * @param userName
     * @param password
     * @return
     */
    AdminUser login(@Param("userName") String userName, @Param("password") String password);

    int deleteBatch(Integer[] ids);

    int edit0Batch(Long ids);

    int edit1Batch(Long ids);
}

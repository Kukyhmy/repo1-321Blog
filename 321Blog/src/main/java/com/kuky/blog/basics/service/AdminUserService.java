package com.kuky.blog.basics.service;

import com.aliyuncs.exceptions.ClientException;
import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.utils.PageQueryUtil;
import com.kuky.blog.basics.utils.PageResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/20 19:38
 */
public interface AdminUserService {

    public String createSmsCode(String phone, HttpServletRequest req) throws ClientException;

    public boolean checkSmsCode(String phone, String code);


    void saveUser(AdminUser user);

    Boolean checkData(String data, Integer type);


    /**
     * 获取用户信息
     * @param username
     * @return
     */
    AdminUser getUserDetailByUsername(String username);

    /**
     * 修改当前登录用户的密码
     * @param username
     * @param originalPassword
     * @param newPassword
     * @return
     */
    boolean updatePassword(String username, String originalPassword, String newPassword);

    /**
     * 修改当前登录用户的名称信息
     * @param username
     * @param loginUserName
     * @param nickName
     * @return
     */
    boolean updateName(String username, String loginUserName, String nickName);

    /**
     * 根据用户名称 查询出所有用户列表
     * @param usernamelist
     * @return
     */
    List<AdminUser> listUsersByUsernames(List<String> usernamelist);

    /**
     * 更改个人设置
     * @param originalUser
     */
    void updateUser(AdminUser originalUser);

    /**
     * 根据id得到当前用户信息
     * @param id
     * @return
     */
    AdminUser getUserById(Long id);

    /**
     * 根据分页条件获得users
     * @param pageUtil
     * @return
     */
    PageResult getUserPage(PageQueryUtil pageUtil);

    int getTotalUsers();

    AdminUser login(String userName, String password);

    boolean deleteBatch(Integer[] ids);

    boolean edit0Batch(Long id);

    boolean edit1Batch(Long id);
}

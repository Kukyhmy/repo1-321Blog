package com.kuky.blog.basics.service;

import com.aliyuncs.exceptions.ClientException;
import com.kuky.blog.basics.entity.AdminUser;

/**
 * @author Kuky
 * @create 2019/7/20 19:38
 */
public interface AdminUserService {

    public String createSmsCode(String phone) throws ClientException;

    public boolean checkSmsCode(String phone, String code);


    void saveUser(AdminUser user);

    Boolean checkData(String data, Integer type);


}

package com.kuky.blog.basics.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.aliyuncs.exceptions.ClientException;
import com.kuky.blog.basics.dao.AdminUserMapper;
import com.kuky.blog.basics.dao.UserAuthorityMapper;
import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.entity.Authority;
import com.kuky.blog.basics.entity.UserAuthority;
import com.kuky.blog.basics.service.AdminUserService;
import com.kuky.blog.basics.utils.MD5Util;
import com.kuky.blog.basics.utils.PageQueryUtil;
import com.kuky.blog.basics.utils.PageResult;
import com.kuky.blog.basics.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * @author Kuky
 * @create 2019/7/20 19:38
 */
@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    BCryptPasswordEncoder bCryptPasswordEncoder =new BCryptPasswordEncoder();
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private UserAuthorityMapper userAuthorityMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;


    @Override
    public String createSmsCode(String phone , HttpServletRequest req) throws ClientException {
        //1、生成一个6为随机验证码
        String smscode = (long)(Math.random()*1000000)+"";
        //2.将验证码放入redis  session
       // req.getSession().setAttribute("smscode", smscode);
        //  redisTemplate.boundHashOps("smscode").put(phone,smscode);
        redisTemplate.opsForValue().set(phone,smscode,5, TimeUnit.MINUTES);

        String s = redisTemplate.opsForValue().get(phone);
        log.info("redis:"+s);
        Map map  = new HashMap();
        map.put("number",smscode);
        smsUtil.sendSms(phone,"SMS_169899873","KUKY", JSONUtils.toJSONString(map));

        return smscode;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
       // String syssmscode = (String) redisTemplate.boundHashOps("smscode").get(phone);
        String syssmscode = redisTemplate.opsForValue().get(phone);
        log.info("注册验证码："+syssmscode);
        if(syssmscode==null){
            return false;
        }
        if(!syssmscode.equals(code)){
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void saveUser(AdminUser user) {
        long min = 1;
        long max = 1000000000;
        long rangeLong = min + (((long) (new Random().nextDouble() * (max - min))));
        user.setId(rangeLong);
        user.setLoginPassword(bCryptPasswordEncoder.encode(user.getLoginPassword()));
        UserAuthority ua = new UserAuthority();
        ua.setUserId(rangeLong);
        ua.setAuthorityId(Long.valueOf("2"));
        adminUserMapper.insertSelective(user);
        userAuthorityMapper.insertSelective(ua);
    }

    @Override
    public void updateUser(AdminUser originalUser) {
        adminUserMapper.updateByPrimaryKeySelective(originalUser);
    }

    @Override
    public AdminUser getUserById(Long id) {
        return adminUserMapper.findByUserId(id);
    }

    @Override
    public Boolean checkData(String data, Integer type) {
        switch (type){
            case 1:
                return adminUserMapper.countByUsername(data)==0;
            case 2:
                return adminUserMapper.countByEmail(data)==0;
            default:
        }
        return null;
    }

    @Override
    public AdminUser getUserDetailByUsername(String username) {
        return adminUserMapper.findByUserName(username)  ;
    }

    @Override
    public boolean updatePassword(String username, String originalPassword, String newPassword) {
      AdminUser adminUser =   adminUserMapper.findByUserName(username);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            String originalPasswordEncoded = bCryptPasswordEncoder.encode(originalPassword);

            String newPasswordMd5Encoded = bCryptPasswordEncoder.encode(newPassword);
            //比较原密码是否正确
            if (originalPasswordEncoded.equals(adminUser.getLoginPassword())) {
                //设置新密码并修改
                adminUser.setLoginPassword(newPasswordMd5Encoded);
                if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                    //修改成功则返回true
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateName(String username, String loginUserName, String nickName) {
        AdminUser adminUser =   adminUserMapper.findByUserName(username);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //设置新密码并修改
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AdminUser> listUsersByUsernames(List<String> usernamelist) {
        return adminUserMapper.listUsersByUsernames(usernamelist);
    }

    @Override
    public PageResult getUserPage(PageQueryUtil pageUtil) {
        List<AdminUser> userList = adminUserMapper.findUserList(pageUtil);
        log.info("用户列表+权限："+userList);
        for(int i = 0 ; i<userList.size();i++){
            AdminUser adminUser = userList.get(i);
            List<Authority> authorityList1 = adminUser.getAuthorityList();
            List<String> authorityList2 = authorityList1.stream().map(Authority::getName).filter(x -> x != null).collect(Collectors.toList());

            adminUser.setAuthorityList2(authorityList2);

            log.info("修改后："+adminUser.getAuthorityList2());
            //userList.add(adminUser);
        }
        int total = adminUserMapper.getTotalUsers(pageUtil);
        PageResult pageResult = new PageResult(userList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public int getTotalUsers() {
        return adminUserMapper.getTotalUsers(null);
    }

    @Override
    public AdminUser login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(userName, passwordMd5);
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        return adminUserMapper.deleteBatch(ids)>0;
    }

    @Override
    public boolean edit0Batch(Long id) {
        return adminUserMapper.edit0Batch(id)>0;
    }
    @Override
    public boolean edit1Batch(Long id) {
        return adminUserMapper.edit1Batch(id)>0;
    }


}

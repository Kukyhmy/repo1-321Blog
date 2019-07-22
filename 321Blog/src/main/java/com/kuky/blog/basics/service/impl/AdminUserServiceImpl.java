package com.kuky.blog.basics.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.aliyuncs.exceptions.ClientException;
import com.kuky.blog.basics.dao.AdminUserMapper;
import com.kuky.blog.basics.dao.UserAuthorityMapper;
import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.entity.UserAuthority;
import com.kuky.blog.basics.service.AdminUserService;
import com.kuky.blog.basics.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private AdminUserMapper amdminUserMapper;

    @Override
    public String createSmsCode(String phone) throws ClientException {
        //1、生成一个6为随机验证码
        String smscode = (long)(Math.random()*1000000)+"";
        System.out.println(smscode);
        //2.将验证码放入redis
        //  redisTemplate.boundHashOps("smscode").put(phone,smscode);
        redisTemplate.opsForValue().set(phone,smscode,5, TimeUnit.MINUTES);

        String s = redisTemplate.opsForValue().get(phone);
        System.out.println(s);
        Map map  = new HashMap();
        map.put("number",smscode);
        smsUtil.sendSms(phone,"SMS_169899873","KUKY", JSONUtils.toJSONString(map));

        return smscode;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String syssmscode = (String) redisTemplate.boundHashOps("smscode").get(phone);
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
        Long id = Long.valueOf(UUID.randomUUID().toString());
        user.setId(id);
        user.setLoginPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserAuthority ua = new UserAuthority();
        ua.setUserId(id);
        ua.setAuthorityId(Long.valueOf("1"));
        userAuthorityMapper.insertSelective(ua);
        amdminUserMapper.insertSelective(user);
    }
    @Override
    public Boolean checkData(String data, Integer type) {
        switch (type){
            case 1:
                return amdminUserMapper.countByUsername(data)==0;
            case 2:
                return amdminUserMapper.countByEmail(data)==0;
            default:
        }
        return null;
    }
}

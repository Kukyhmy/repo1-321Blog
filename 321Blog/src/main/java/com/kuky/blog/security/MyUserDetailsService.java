/**
 * 
 */
package com.kuky.blog.security;

import com.kuky.blog.basics.dao.AdminUserMapper;
import com.kuky.blog.basics.dao.UserAuthorityMapper;
import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.entity.Authority;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UserDetailsService：处理用户信息获取逻辑
 * 通过username构建UserDetails对象，通过loadUserByUsername根据userName获取UserDetail对象
 * 用户登录的service实现类
 * 框架的默认实现是 JdbcDaoImpl
 */
@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理密码加密解密
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
    private AdminUserMapper adminUserMapper;

	@Autowired
	private UserAuthorityMapper userAuthorityMapper;
	/*
	 * (non-Javadoc)
	 * 
	 * 处理用户校验逻辑
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {
		// 根据用户名查找用户信息
		//根据查找到的用户信息判断用户是否被冻结
		String permissions = "";
		AdminUser adminUser = adminUserMapper.findByUserName(username);
		if (adminUser != null) {
			//permissions = adminUserMapper.findAuthorityByUserName(adminUser.getUsername());
			List<Authority> authorityList = adminUser.getAuthorityList();
			log.info(permissions);
			String authoritis = authorityList.stream().map(Authority::getName).toString();
			return new AdminUser(adminUser.getLoginUserName(), adminUser.getLoginPassword(), AuthorityUtils.
					commaSeparatedStringToAuthorityList(authoritis));
		}else{
			throw new BadCredentialsException("用户名不存在！");
		}

	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		AdminUser user = adminUserMapper.findByUserName(userId);
		log.info("通过userId得到的用户信息----->"+user);
		return user;

	}




}

/**
 * 
 */
package com.kuky.blog.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 *自动生成userId
 *
 */
@Component
public class DemoConnectionSignUp implements ConnectionSignUp {


	@Override
	public String execute(Connection<?> connection) {
		//根据社交用户信息默认创建用户并返回用户唯一标识
		return connection.getDisplayName();
	}

}

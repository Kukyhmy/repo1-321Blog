package com.kuky.blog.core.authentication;

import com.kuky.blog.core.properties.SecurityConstants;
import com.kuky.blog.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.kuky.blog.core.properties.SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM;


public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
	
	@Autowired
	protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;
	
	protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
		http.formLogin()
			.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL).failureUrl("/login?error=true")
				.loginProcessingUrl(DEFAULT_LOGIN_PROCESSING_URL_FORM)
			.successHandler(imoocAuthenticationSuccessHandler)
			.failureHandler(imoocAuthenticationFailureHandler);
	}
	/*@Override
	public void configure(WebSecurity web) throws Exception {
		//解决静态资源被SpringSecurity拦截的问题
		web.ignoring().antMatchers("/static/**");
	}*/
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());;
	}
}

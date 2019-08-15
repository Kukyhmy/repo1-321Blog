package com.kuky.blog.browser;

import javax.sql.DataSource;

import com.kuky.blog.browser.filter.ValidateCodeFilter;
import com.kuky.blog.core.authentication.AbstractChannelSecurityConfig;
import com.kuky.blog.core.properties.SecurityProperties;
import com.kuky.blog.core.social.BlogSpringSocialConfigurer;
import com.kuky.blog.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.security.SpringSocialConfigurer;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法安全设置
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {
	private static final String KEY = "321kuky.com";
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private SpringSocialConfigurer blogSocialSecurityConfig;
	
	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
	
	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private ValidateCodeFilter validateCodeFilter;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		applyPasswordAuthenticationConfig(http);
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.apply(blogSocialSecurityConfig)
				.and()
			.rememberMe().key(KEY)
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds()) // 有效时间：单位s
				.userDetailsService(userDetailsService)
				.and()
			.sessionManagement()
				.invalidSessionStrategy(invalidSessionStrategy) //session无效处理策略
				.maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
				.maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
				.expiredSessionStrategy(sessionInformationExpiredStrategy)//session过期处理策略
				.and()
				.and()
				.logout()
				.logoutUrl("/signOut")
				.logoutSuccessUrl("/")
				//.logoutSuccessHandler(logoutSuccessHandler)
				.deleteCookies("JSESSIONID")
				.and()
			.authorizeRequests()
                .antMatchers("/admin/login").permitAll()
                .antMatchers("/admin/dist/**").permitAll()
                .antMatchers("/admin/plugins/**").permitAll()
				.antMatchers("/bolg/comment").hasRole("USER")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest()
				.permitAll()//所有的路径即可访问
				/*.antMatchers(
					SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
					securityProperties.getBrowser().getLoginPage(),
					securityProperties.getBrowser().getSignUpUrl(),
					securityProperties.getBrowser().getSignOutUrl(),
					securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".json",
					securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".html",
					"/","/index","/user/**","/goIndex","/fileUpload","/uploadFileToFast","/regist","/login","/user/**","/321blog/**","/common/**","/css/**","/js/**","/images/**","/static/**","/connect/**")
					.permitAll()*/
				//.anyRequest()
				//.authenticated()
				.and()
			.csrf().disable();
		
	}



	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
	}
	/**
	 * 实现记住我登录
	 * @return
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		// 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Autowired//注意这个方法是注入的
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}


	@Bean("sessionStrategy")
	public SessionStrategy registBean() {
		SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
		return sessionStrategy;
	}


	/*@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder); // 设置密码加密方式
		return authenticationProvider;
	}*/

}

/**
 * 
 */
package com.kuky.blog.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
  * 继承默认的社交登录配置，加入自定义的后处理逻辑
  @author Kuky
  * @create 2019/7/2 19:48
 */
public class BlogSpringSocialConfigurer extends SpringSocialConfigurer {
	
	private String filterProcessesUrl;
	
	public BlogSpringSocialConfigurer(String filterProcessesUrl) {
		this.filterProcessesUrl = filterProcessesUrl;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T postProcess(T object) {
		SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
		filter.setFilterProcessesUrl(filterProcessesUrl);
		filter.setSignupUrl("/socialRegister");
		return (T) filter;
	}

}

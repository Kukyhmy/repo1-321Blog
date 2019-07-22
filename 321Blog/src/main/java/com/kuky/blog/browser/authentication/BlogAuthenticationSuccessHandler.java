/**
 * 
 */
package com.kuky.blog.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuky.blog.basics.vo.Response;
import com.kuky.blog.core.properties.LoginResponseType;
import com.kuky.blog.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhailiang
 *
 */
@Component("blogAuthenticationSuccessHandler")
public class BlogAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityProperties securityProperties;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		logger.info("登录成功");

		if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(new Response(false,authentication.getName())));
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
		RequestCache requestCache = new HttpSessionRequestCache();
		String url = null;
		SavedRequest savedRequest = requestCache.getRequest(request,response);
		if(savedRequest != null){
			url = savedRequest.getRedirectUrl();
		}
		if(url == null){
			getRedirectStrategy().sendRedirect(request,response,"/");
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

}

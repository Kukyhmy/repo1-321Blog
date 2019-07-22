package com.kuky.blog.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuky.blog.basics.vo.Response;
import com.kuky.blog.core.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Kuky
 * @create 2019/7/13 23:15
 */
public class BlogLogoutSuccessHandler implements LogoutSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();
    private String signOutUrl;
    public BlogLogoutSuccessHandler(String signOutUrl){
        this.signOutUrl = signOutUrl;
    }
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("退出成功");

        if(StringUtils.isBlank(signOutUrl)){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new Response(true,"退出登录成功") ));
        }else {
            response.sendRedirect(signOutUrl);
        }
    }
}

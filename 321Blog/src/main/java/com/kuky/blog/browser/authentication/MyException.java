package com.kuky.blog.browser.authentication;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Kuky
 * @create 2019/7/25 13:55
 */
public class MyException  extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public MyException(String msg) {
        super(msg);
    }


}

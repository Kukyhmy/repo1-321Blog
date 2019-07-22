package com.kuky.blog.core.social;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;

/**
 * @author Kuky
 * @create 2019/7/16 15:00
 */
public class AuthenticationNameUserIdSource  implements UserIdSource {

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return authentication.getName();
    }

}

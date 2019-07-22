package com.kuky.blog.basics.entity;

import lombok.Data;

@Data
public class UserAuthority {

    private Long id;
    private Long userId;

    private Long authorityId;

    private AdminUser adminUser;

    private Authority authority;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }
}
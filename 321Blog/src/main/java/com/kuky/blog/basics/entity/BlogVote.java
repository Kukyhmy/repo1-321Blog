package com.kuky.blog.basics.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BlogVote {
    private Long id;

    private Date createTime;

    private Long blogId;





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }
}
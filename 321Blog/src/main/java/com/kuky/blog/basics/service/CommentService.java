package com.kuky.blog.basics.service;

import com.kuky.blog.basics.entity.BlogComment;

/**
 * @author Kuky
 * @create 2019/7/21 19:37
 */
public interface CommentService {
    /**
     * 添加评论
     *
     * @param blogComment
     * @return
     */
    Boolean addComment(BlogComment blogComment);
}

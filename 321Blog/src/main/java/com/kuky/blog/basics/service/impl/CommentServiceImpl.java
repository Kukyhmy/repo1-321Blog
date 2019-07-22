package com.kuky.blog.basics.service.impl;

import com.kuky.blog.basics.dao.BlogCommentMapper;
import com.kuky.blog.basics.entity.BlogComment;
import com.kuky.blog.basics.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kuky
 * @create 2019/7/21 19:37
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Override
    public Boolean addComment(BlogComment blogComment) {
        return blogCommentMapper.insertSelective(blogComment) > 0;
    }
}

package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogComment;
import org.springframework.stereotype.Component;

/**
 * @author Kuky
 * @create 2019/7/21 19:53
 */
@Component
public interface BlogCommentMapper {

    int insertSelective(BlogComment record);
}

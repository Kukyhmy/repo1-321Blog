package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogComment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Kuky
 * @create 2019/7/21 19:53
 */
@Component
public interface BlogCommentMapper {

    int insertSelective(BlogComment record);

    int getTotalBlogComments(Map map);

    List<BlogComment> findBlogCommentList(Map map);

    int checkDone(Integer[] ids);

    BlogComment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(BlogComment record);

    int deleteBatch(Integer[] ids);
}

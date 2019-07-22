package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.Blog;
import com.kuky.blog.basics.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/21 22:18
 */
@Repository
public interface BlogMapper {

    List<Blog> findBlogList(PageQueryUtil pageUtil);

    int getTotalBlogs(PageQueryUtil pageUtil);

    List<Blog> findBlogListByType(@Param("type") int type, @Param("limit") int limit);
}

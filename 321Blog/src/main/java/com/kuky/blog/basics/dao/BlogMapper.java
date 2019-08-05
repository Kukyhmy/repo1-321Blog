package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.Blog;
import java.util.List;

import com.kuky.blog.basics.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogMapper {
    List<Blog> findBlogList(PageQueryUtil pageUtil);

    int getTotalBlogs(PageQueryUtil pageUtil);

    List<Blog> findBlogListByType(@Param("type") int type, @Param("limit") int limit);

    Blog selectByPrimaryKey(Long blogId);

    int updateByPrimaryKey(Blog record);

    List<Blog> getBlogsPageByTagId(PageQueryUtil pageUtil);

    int getTotalBlogsByTagId(PageQueryUtil pageUtil);

    Blog selectBySubUrl(String subUrl);

    int insertSelective(Blog record);

    void increaseVoteSize(Long blogId);

    int deleteBatch(Integer[] ids);

    int updateByPrimaryKeySelective(Blog record);

    int updateBlogCategorys(@Param("categoryName") String categoryName, @Param("categoryId") Integer categoryId, @Param("ids")Integer[] ids);
}
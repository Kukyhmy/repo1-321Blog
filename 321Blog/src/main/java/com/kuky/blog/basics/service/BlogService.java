package com.kuky.blog.basics.service;

import com.kuky.blog.basics.entity.Blog;
import com.kuky.blog.basics.utils.PageResult;
import com.kuky.blog.basics.vo.BlogDetailVO;
import com.kuky.blog.basics.vo.BlogListVO;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/21 22:12
 */
public interface BlogService {
    /**
     * 获取首页文章列表
     *
     * @param page
     * @return
     */
    PageResult getBlogsForIndexPage(int page);

    /**
     * 首页侧边栏数据列表
     * 0-最热 1-最新
     *
     * @param type
     * @return
     */
    List<BlogListVO> getBlogListForIndexPage(int type);

    /**
     * 文章详情
     *
     * @param blogId
     * @return
     */
    BlogDetailVO getBlogDetail(Long blogId);

    /**
     * 根据标签获取文章列表
     *
     * @param tagName
     * @param page
     * @return
     */
    PageResult getBlogsPageByTag(String tagName, int page);

    /**
     * 根据分类获取文章列表
     *
     * @param categoryId
     * @param page
     * @return
     */
    PageResult getBlogsPageByCategory(String categoryId, int page);

    /**
     * 根据搜索获取文章列表
     *
     * @param keyword
     * @param page
     * @return
     */
    PageResult getBlogsPageBySearch(String keyword, int page);

    BlogDetailVO getBlogDetailBySubUrl(String subUrl);

    int getTotalBlogs();

    String saveBlog(Blog blog);

    void increaseVoteSize(Long blogId);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 后台修改
     *
     * @param blog
     * @return
     */
    String updateBlog(Blog blog);
}

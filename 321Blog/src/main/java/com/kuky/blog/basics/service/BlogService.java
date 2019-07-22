package com.kuky.blog.basics.service;

import com.kuky.blog.basics.utils.PageResult;
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
}

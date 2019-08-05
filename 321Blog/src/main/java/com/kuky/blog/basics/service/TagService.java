package com.kuky.blog.basics.service;

import com.kuky.blog.basics.entity.BlogTagCount;
import com.kuky.blog.basics.utils.PageQueryUtil;
import com.kuky.blog.basics.utils.PageResult;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/22 0:35
 */
public interface TagService {

    List<BlogTagCount> getBlogTagCountForIndex();

    int getTotalTags();
    /**
     * 查询标签的分页数据
     *
     * @param pageUtil
     * @return
     */
    PageResult getBlogTagPage(PageQueryUtil pageUtil);

    Boolean saveTag(String tagName);

    Boolean deleteBatch(Integer[] ids);
}

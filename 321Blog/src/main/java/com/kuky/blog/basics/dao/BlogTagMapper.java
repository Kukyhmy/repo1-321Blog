package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogTag;
import com.kuky.blog.basics.entity.BlogTagCount;
import com.kuky.blog.basics.utils.PageQueryUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/22 18:25
 */
@Repository
public interface BlogTagMapper {


    List<BlogTagCount> getTagCount();

    BlogTag selectByTagName(String tagName);

    int getTotalTags(PageQueryUtil pageUtil);

    int batchInsertBlogTag(List<BlogTag> tagList);

    List<BlogTag> findTagList(PageQueryUtil pageUtil);

    int insertSelective(BlogTag record);

    int deleteBatch(Integer[] ids);
}

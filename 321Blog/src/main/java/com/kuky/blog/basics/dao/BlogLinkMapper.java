package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogLink;
import com.kuky.blog.basics.utils.PageQueryUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/23 22:42
 */
@Component
public interface BlogLinkMapper {

    List<BlogLink> findLinkList(PageQueryUtil pageUtil);

    int getTotalLinks(PageQueryUtil pageUtil);

    int insertSelective(BlogLink record);

    BlogLink selectByPrimaryKey(Integer linkId);

    int updateByPrimaryKeySelective(BlogLink record);

    int deleteBatch(Integer[] ids);

    List<BlogLink> selectLink();
}

package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/21 22:51
 */
@Repository
public interface BlogCategoryMapper {

    List<BlogCategory> selectByCategoryIds(List<Integer> categoryIds);
}

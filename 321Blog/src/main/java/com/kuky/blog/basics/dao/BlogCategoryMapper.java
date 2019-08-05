package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogCategory;
import com.kuky.blog.basics.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/21 22:51
 */
@Repository
public interface BlogCategoryMapper {

    List<BlogCategory> selectByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

    BlogCategory selectByPrimaryKey(Integer categoryId);

    /**
     * 获取分类列表
     * 按照创建时间、顺序（降序）
     * @param pageUtil
     * @return
     */
    List<BlogCategory> findCategoryList(PageQueryUtil pageUtil);

    BlogCategory selectByCategoryName(String categoryName);

    /**
     * 获取所有未被删除的分类记录条数
     * @param pageUtil
     * @return
     */
    int getTotalCategories(PageQueryUtil pageUtil);

    int updateByPrimaryKeySelective(BlogCategory record);

    int insertSelective(BlogCategory record);
    int deleteBatch(Integer[] ids);
}

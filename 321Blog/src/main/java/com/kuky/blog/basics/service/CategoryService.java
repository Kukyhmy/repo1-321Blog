package com.kuky.blog.basics.service;

import com.kuky.blog.basics.entity.BlogCategory;
import com.kuky.blog.basics.utils.PageQueryUtil;
import com.kuky.blog.basics.utils.PageResult;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/23 16:50
 */
public interface CategoryService {

    List<BlogCategory> getAllCategories();

    /**
     * 获取分类条数
     * @return
     */
    int getTotalCategories();

    /**
     * 查询分类的分页数据
     *
     * @param pageUtil
     * @return
     */
    PageResult getBlogCategoryPage(PageQueryUtil pageUtil);

    /**
     * 添加分类数据
     *
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    Boolean saveCategory(String categoryName,String categoryIcon);

    Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    Boolean deleteBatch(Integer[] ids);

    BlogCategory getCategoryById(Integer categoryId);
}

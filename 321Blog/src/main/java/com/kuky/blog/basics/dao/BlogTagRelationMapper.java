package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogTagRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/26 10:27
 */
@Repository
public interface BlogTagRelationMapper {


    /**
     *
     * @param tagIds
     * @return
     */
    List<Long> selectDistinctTagIds(Integer[] tagIds);
    int batchInsert(@Param("relationList") List<BlogTagRelation> blogTagRelationList);

    int deleteByBlogId(Long blogId);
}

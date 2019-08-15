package com.kuky.blog.basics.dao;

import com.kuky.blog.basics.entity.BlogVote;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogVoteMapper {


    int insert(BlogVote record);

    int insertSelective(BlogVote record);



}
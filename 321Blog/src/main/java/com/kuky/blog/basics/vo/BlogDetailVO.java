package com.kuky.blog.basics.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/22 21:50
 */
@Data
public class BlogDetailVO {
    /**
     * *****
     */
    private Integer commentCount;

    /**
     * *****
     */
    private String blogCategoryIcon;

    private Long blogId;

    private String blogTitle;

    private String blogSummary;

    private Integer blogCategoryId;

    private String blogCategoryName;

    private String blogCoverImage;

    private Long blogViews;

    private List<String> blogTags;

    private String blogContent;

    private Byte enableComment;

    private Date createTime;

    private Integer blogVoteSize;
}

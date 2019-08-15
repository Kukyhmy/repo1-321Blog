package com.kuky.blog.basics.entity;

import lombok.Data;

/**
 * @author Kuky
 * @create 2019/7/22 18:29
 */
@Data
public class BlogTagCount {

    private Integer tagId;

    private String tagName;

    private Integer tagCount;
}

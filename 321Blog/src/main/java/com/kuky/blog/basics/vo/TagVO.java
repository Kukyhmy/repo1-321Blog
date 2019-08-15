package com.kuky.blog.basics.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kuky
 * @create 2019/7/28 15:38
 */
@Data
public class TagVO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Long count;

    public TagVO(String name, Long count) {
        this.name = name;
        this.count = count;
    }
}
package com.kuky.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Kuky
 * @create 2019/7/6 14:49
 */
@Controller
public class UserController {

    @RequestMapping("/test1")
    public String test(){
        return "test";
    }
}

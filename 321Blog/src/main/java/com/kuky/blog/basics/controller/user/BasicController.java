package com.kuky.blog.basics.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kuky
 * @create 2019/7/13 12:46
 */
@Controller()
public class BasicController {

@RequestMapping("/login")
public String login(){
        return "321blog/mobanzhijia/user-login";
}

    @GetMapping("/message/{msg}")
    public String message(@PathVariable("msg") String msg, HttpServletRequest request, HttpServletResponse response, Model model) {
        String message="解绑成功！";
        if("bindingSuccess".equals(msg)){
            message="绑定成功！";
        }
        model.addAttribute("msg",message);
        model.addAttribute("url","/index");
        return "msg-success";
    }


    @RequestMapping("/regist")
    public String regist(){
        return "regist";
    }


}

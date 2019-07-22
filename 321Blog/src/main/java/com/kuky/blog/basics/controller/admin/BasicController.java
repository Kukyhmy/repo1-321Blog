package com.kuky.blog.basics.controller.admin;

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
//@RequestMapping("/basic")
public class BasicController {

    /**
     * 主页
     *
     * @param userDetails
     * @return
     */
    @GetMapping(value = {"/index", "/"})
    //@AuthenticationPrincipal UserDetails userDetails,
    public String index(Model model) {
        //model.addAttribute("username",userDetails.getUsername());
        return "index";

    }

    @GetMapping("/message/{msg}")
    public String message(@PathVariable("msg") String msg, HttpServletRequest request, HttpServletResponse response, Model model) {
        String message="解绑成功！";
        if("bindingSuccess".equals(msg)){
            message="绑定成功！";
        }
        model.addAttribute("msg",message);
        model.addAttribute("url","/index");
        return "success";
    }

    @RequestMapping("/login")
    public String test(){
        return "login";
    }
    @RequestMapping("/regist")
    public String regist(){
        return "regist1";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，账号或者密码错误！");
        return "login";
    }
}

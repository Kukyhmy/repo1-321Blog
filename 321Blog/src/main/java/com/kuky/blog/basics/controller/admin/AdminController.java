package com.kuky.blog.basics.controller.admin;

import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.service.*;
import com.kuky.blog.basics.utils.PageQueryUtil;
import com.kuky.blog.basics.utils.PatternUtil;
import com.kuky.blog.basics.utils.Result;
import com.kuky.blog.basics.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuky
 * @create 2019/7/24 23:14
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {


    @Resource
    private AdminUserService adminUserService;
    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;
    @Resource
    private AuthorityService authorityService;

    @GetMapping("/users")
    public String tagPage(HttpServletRequest request) {
        request.setAttribute("path", "users");
        return "admin/user";
    }

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @GetMapping({"/test"})
    public String test() {
        return "admin/test";
    }


    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest req) {
        req.setAttribute("path", "index");
        req.setAttribute("categoryCount", categoryService.getTotalCategories());
        req.setAttribute("blogCount", blogService.getTotalBlogs());
        req.setAttribute("linkCount", linkService.getTotalLinks());
        req.setAttribute("tagCount", tagService.getTotalTags());
        req.setAttribute("userCount",adminUserService.getTotalUsers());
        req.setAttribute("commentCount", commentService.getTotalComments());
        req.setAttribute("path", "index");
        return "admin/index";
    }

    /**
     * 管理员登录
     * @param userName
     * @param password
     * @param verifyCode
     * @param session
     * @return
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        AdminUser adminUser = adminUserService.login(userName, password);
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getId());
            //session过期时间设置为7200秒 即两小时
            //session.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "登陆失败");
            return "admin/login";
        }
    }
    @DeleteMapping ("/users/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (adminUserService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败了~~~~~~~");
        }
    }
    @PutMapping("/users/edit/{id}/{locked}")
    @ResponseBody
    public Result edit(@PathVariable("id") Long id, @PathVariable("locked") Byte locked) {

        if(locked==0){
            if (adminUserService.edit0Batch(id)) {
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("修改失败了~~~~~~~");
            }
        }else{
            if (adminUserService.edit1Batch(id)) {
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("修改失败了~~~~~~~");
            }
        }
    }
    @DeleteMapping("/users/delete/{id}/{authorityName}")
    @ResponseBody
    public Result delete(@PathVariable("id") Long id, @PathVariable("authorityName") String name) {
        //验证参数
        if(!PatternUtil.isAuthorityName(name)){
            return ResultGenerator.genFailResult("参数异常！");
        }
        if(!StringUtils.isEmpty(name)&&name.equals("ROLE_ADMIN")){
            if(authorityService.deleteAuthority(id,1l)){
                return ResultGenerator.genSuccessResult();
            }else {
                return ResultGenerator.genFailResult("删除失败！");
            }
        }else if(!StringUtils.isEmpty(name)&&name.equals("ROLE_USER")){
            if(authorityService.deleteAuthority(id,2l)){
                return ResultGenerator.genSuccessResult();
            }else {
                return ResultGenerator.genFailResult("删除失败！");
            }
        }else {
            if(authorityService.deleteAuthority(id,3l)){
                return ResultGenerator.genSuccessResult();
            }else {
                return ResultGenerator.genFailResult("删除失败！");
            }
        }

    }

    @PostMapping("/user/save/{authorityName}")
    @ResponseBody
    public Result save(@PathVariable("authorityName")String name,@RequestBody Integer[] ids){
        //验证参数
        if(!PatternUtil.isAuthorityName(name)){
            return ResultGenerator.genFailResult("参数异常！");
        }
        //由于主键是自增 ---》先判断  在插入 避免重复
        log.info("name:"+name);
        if(!StringUtils.isEmpty(name)&&name.equals("ROLE_ADMIN")){
            Map<Long,Integer> map  = new HashMap<>();
            for (int i = 0; i < ids.length; i++) {
                map.put(1l,ids[i]);
            }
            if(authorityService.addAuthority(map)){
             return ResultGenerator.genSuccessResult();
         } else {
             return ResultGenerator.genFailResult("权限名称重复，该用户已有！");
         }
        }else if(!StringUtils.isEmpty(name)&&name.equals("ROLE_USER")){
            Map<Long,Integer> map  = new HashMap<>();
            for (int i = 0; i < ids.length; i++) {
                map.put(2l,ids[i]);
            }
            boolean b = authorityService.addAuthority(map);
            if(b){
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("权限名称重复，该用户已有！");
            }
        }else {
            Map<Long,Integer> map  = new HashMap<>();
            for (int i = 0; i < ids.length; i++) {
                map.put(3l,ids[i]);
            }
            boolean b = authorityService.addAuthority(map);
            if(b){
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("权限名称重复，该用户已有！");
            }
        }
    }
    /**
     * 查询所用用户
     * @return
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(adminUserService.getUserPage(pageUtil));
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest req,@AuthenticationPrincipal UserDetails user) {

        AdminUser adminUser = adminUserService.getUserDetailByUsername(user.getUsername());
        if (adminUser == null) {
            return "admin/login";
        }
        req.setAttribute("path", "profile");
        req.setAttribute("loginUserName", adminUser.getLoginUserName());
        req.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest req, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword,@AuthenticationPrincipal UserDetails user) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
            if (adminUserService.updatePassword(user.getUsername(), originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
                req.getSession().removeAttribute("loginUserId");
                req.getSession().removeAttribute("loginUser");
                req.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest req, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName,@AuthenticationPrincipal UserDetails user) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        if (adminUserService.updateName(user.getUsername(), loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }


}


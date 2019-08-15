
package com.kuky.blog.basics.controller.user;

import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.entity.Blog;
import com.kuky.blog.basics.entity.BlogCategory;
import com.kuky.blog.basics.service.*;
import com.kuky.blog.basics.utils.PageResult;
import com.kuky.blog.basics.utils.PhoneFormatCheckUtils;
import com.kuky.blog.basics.utils.ResultGenerator;
import com.kuky.blog.basics.vo.Response;
import com.kuky.blog.security.MyUserDetailsService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Kuky
 * @create 2019/7/9 11:44
 */
@Controller
public class UserController {
	
	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private BlogService blogService;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private CategoryService categoryService;



	/**
	 * 获取编辑头像的界面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView avatar(@PathVariable("username") String username, Model model) {
		AdminUser user = adminUserService.getUserDetailByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/321blog/mobanzhijia/avatar", "userModel", model);
	}


	/**
	 * 保存头像
	 * @param username
	 * @return
	 */
	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
	public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody AdminUser user) {
		String avatarUrl = user.getAvatar();

		AdminUser originalUser = adminUserService.getUserById(user.getId());
		originalUser.setAvatar(avatarUrl);
		adminUserService.saveUser(originalUser);

		return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
	}


	@GetMapping("/u/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView profile(@PathVariable("username") String username, Model model,HttpServletRequest req) {
		AdminUser user = adminUserService.getUserDetailByUsername(username);
		model.addAttribute("user", user);
		req.setAttribute("configurations", configService.getAllConfigs());
		return new ModelAndView("/321blog/mobanzhijia/profile2", "userModel", model);
	}


	/**
	 * 保存个人设置
	 * @return
	 */
	@PostMapping("/u/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
	public String saveProfile(@PathVariable("username") String username,AdminUser adminUser) {
		AdminUser originalUser = adminUserService.getUserById(adminUser.getId());
		originalUser.setEmail(adminUser.getEmail());
		originalUser.setNickName(adminUser.getNickName());

		// 判断密码是否做了变更
		String rawPassword = originalUser.getPassword();
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePasswd = encoder.encode(adminUser.getPassword());
		boolean isMatch = encoder.matches(rawPassword, encodePasswd);
		if (!isMatch) {
			originalUser.setEncodePassword(adminUser.getPassword());
		}

		adminUserService.updateUser(originalUser);
		return "redirect:/" + username + "/profile";
	}

	/**
	 * 利用providerSignInUtils 完成将userId和社交账号信息插入到userconnection表里面
	 * 用户第二次完成注册后，如果表里面有userId了，就会进行注册的登录认证流程
	 * @param user
	 * @param request
	 */
	@PostMapping("/register")
	public String regist(AdminUser user, HttpServletRequest request) {
		//不管是注册用户还是绑定用户，都会拿到一个用户唯一标识。
		String userId = user.getLoginUserName();
		providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
		return "redirect:/index";
	}
	@ResponseBody
	@GetMapping("/me")
	public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
		return user;
	}


	/**
	 * 验证手机格式+发送验证码
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@PostMapping("/code")
	public ResponseEntity<Response> sendCode(@RequestParam("phone") String phone,HttpServletRequest req){

		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
			return ResponseEntity.ok().body(new Response(false, "手机格式不正确", null));
		}
		try{

			String code = adminUserService.createSmsCode(phone,req);
			return ResponseEntity.ok().body(new Response(true,"验证码发送成功，请查收"));
		}catch (Exception e){
			return  ResponseEntity.ok().body(new Response(false,"验证码发送失败"));
		}

	}

	/**
	 * 用户注册
	 * @param user
	 * @param smscode
	 * @param request
	 * @return
	 */
	@PostMapping("/regist/{authcode}")
	public ResponseEntity<Response> add(AdminUser user, @PathVariable("authcode") String smscode, HttpServletRequest request){

		System.out.println(user);
		boolean checkSmsCode = adminUserService.checkSmsCode(user.getPhone(), smscode);
		if(!checkSmsCode){
			return ResponseEntity.ok().body(new Response(false,"验证码不正确"));
		}
        //不管是注册用户还是绑定用户，都会拿到一个用户唯一标识。
		System.out.println(smscode);
		String userId = user.getLoginUserName();
		providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
		adminUserService.saveUser(user);
		return ResponseEntity.ok().body(new Response(true, "注册成功!"));
	}

	/**
	 * 校验数据唯一性  type=1验用户名  =2验邮箱
	 * @param data
	 * @param type
	 * @return
	 */
	@ResponseBody
	@GetMapping("/check/{data}/{type}")
	public ResponseEntity<Response> check(@PathVariable("data")String data,@PathVariable("type")Integer type){
		Boolean aBoolean = adminUserService.checkData(data, type);
		if(aBoolean){
			return ResponseEntity.ok().body(new Response(true,"可用"));
		}else{
			return ResponseEntity.ok().body(new Response(false,"不可用"));
		}
	}






}

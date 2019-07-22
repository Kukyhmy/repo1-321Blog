/**
 * 
 */
package com.kuky.blog.basics.controller.admin;

import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.service.AdminUserService;
import com.kuky.blog.basics.service.AuthorityService;
import com.kuky.blog.basics.utils.PhoneFormatCheckUtils;
import com.kuky.blog.basics.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhailiang
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@Autowired
	private AdminUserService adminUserService;


	@Autowired
	private AuthorityService authorityService;

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
	public ResponseEntity<Response> sendCode(@RequestParam("phone") String phone){

		System.out.println(phone);
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
			return ResponseEntity.ok().body(new Response(false, "手机格式不正确", null));
		}
		try{

			String code = adminUserService.createSmsCode(phone);
			return ResponseEntity.ok().body(new Response(true,"验证码发送成功，请查收",code));
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
	@PostMapping("/regist/{smscode}")
	public ResponseEntity<Response> add(AdminUser user, String smscode, HttpServletRequest request){

		boolean checkSmsCode = adminUserService.checkSmsCode(user.getPhone(), smscode);
		if(!checkSmsCode){
			return ResponseEntity.ok().body(new Response(false,"验证码不正确"));
		}
        //不管是注册用户还是绑定用户，都会拿到一个用户唯一标识。
		String userId = user.getLoginUserName();
		providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
		adminUserService.saveUser(user);
		return ResponseEntity.ok().body(new Response(true, "注册成功", user));
	}

	/**
	 * 校验数据唯一性  type=1验用户名  =2验邮箱
	 * @param data
	 * @param type
	 * @return
	 */
	@ResponseBody
	@GetMapping("/check/{data}/{type}")
	public Boolean check(@PathVariable("data")String data,@PathVariable("type")Integer type){
		return adminUserService.checkData(data,type);
	}






}

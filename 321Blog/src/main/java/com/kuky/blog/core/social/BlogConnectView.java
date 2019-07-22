/**
 * 
 */
package com.kuky.blog.core.social;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 *绑定成功后返回的页面
 */
public class BlogConnectView extends AbstractView {


	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String msg = "";
		response.setContentType("text/html;charset=UTF-8");
		if (model.get("connection") == null) {
			msg = "unBindingSuccess";
			//response.getWriter().write("<h3>解绑成功</h3>");
		} else {
			msg = "bindingSuccess";
			//response.getWriter().write("<h3>绑定成功</h3>");
		}
		response.sendRedirect("/message/" + msg);
	}

}

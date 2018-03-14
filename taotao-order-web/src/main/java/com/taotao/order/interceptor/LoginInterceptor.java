package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 判断用户是否登录拦截器
 * <p>Title: LoginInterceptor</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		执行handler之前执行此方法
//		1、从cookie中取token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
//		2、如果取不到token，跳转sso登录页面，需要把当前请求的URL作为参数传递给sso，登录成功后跳转请求页面
		if(StringUtils.isBlank(token)) {
//			取当前请求url
			String requestURL = request.getRequestURL().toString();
//			跳转登录页面
			response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
//			拦截
			return false;
		}
//		3、取到token，调用sso系统的服务判断是否登录
		TaotaoResult taotaoResult = userService.getUserByToken(token);
//		4、如果未登录，跳转sso登录页面
		if(taotaoResult.getStatus() != 200) {
			String requestURL = request.getRequestURL().toString();
			response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
			return false;
		}
		
//		如果取到用户信息，放行
//		把用户信息放到request中
		TbUser user = (TbUser) taotaoResult.getData();
		request.setAttribute("user", user);
		return true;
	}

}

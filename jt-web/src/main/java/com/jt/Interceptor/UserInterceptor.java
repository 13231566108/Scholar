package com.jt.Interceptor;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class UserInterceptor implements HandlerInterceptor {
	/**
	 * boolean:
	 * 		true: 请求放行
	 * 		false：请求拦截 需要配合重定向联用
	 */
	@Autowired
	private JedisCluster jedisCluster;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Cookie[] cookies = request.getCookies();
		if(cookies == null || cookies.length == 0){
			// 重定向到用户登录的页面
			response.sendRedirect("/user/login.html");
			return false;
		}
		String ticket = null;
		for (Cookie cookie:cookies
			 ) {
			if("JT_TICKET".equals(cookie.getName())){
				ticket = cookie.getValue();
				break;
			}
		}

		if(StringUtils.isEmpty(ticket)){

			// 重定向到用户登录的页面
			response.sendRedirect("/user/login.html");
			return false;
		}

		String userJson = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJson)){
			CookieUtil.deleteCookie("JT_TICKET",response);
			response.sendRedirect("/user/login.html");
			return false;
		}

		// 方式一 request对象传参
		User user = ObjectMapperUtil.toObj(userJson,User.class);
//		request.setAttribute("JT_USER",user);

		// 方式二 ThreadLocal 本地线程变量
		UserThreadLocal.set(user);

		return true;
	}
}

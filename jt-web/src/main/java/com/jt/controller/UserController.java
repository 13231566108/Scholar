package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Reference(check = false)
	private DubboUserService dubboUserService;

	@Autowired
	private JedisCluster jedisCluster;

	@RequestMapping("/doRegister")
	@ResponseBody // 转化为json
	public SysResult saveUser(User user){
		dubboUserService.saveUser(user);//rpc调用
		return SysResult.success();
	}

	@RequestMapping("{moduleName}")
	public String module(@PathVariable String moduleName){

		return moduleName;
	}

	/**
	 * 1 发起rpc请求校验用户数据
	 * 2 如果返回值为null 用户名和密码错误
	 * 3 如果返回值不为null
	 * @param user
	 * @return
	 *
	 * 关于Cookie的说明
	 * 	1 setPath("/")  Cookie权限的路径
	 * 	   url1:http://www.jt.com/a.html
	 * 	   url2:http://www.jt.com/user/b.html
	 *
	 * 	2 setMaxAge  设定Cookie超时时间
	 * 		>0	有超时时间  单位秒
	 * 		=0  要求立即删除Cookie
	 * 		-1  关闭会话后，删除cookie
	 * 	3 cookie的特性
	 * 		1 Cookie与域名绑定的
	 * 		2 一个域名只能看到自己的cookie记录
	 * 	    3 request对象只能获取cookie的简单记录 K:V
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user, HttpServletResponse response){

		String ticket = dubboUserService.findUserByUP(user);
		if(StringUtils.isEmpty(ticket)){
			return SysResult.fail();
		}

		// 说明用户名和密码正确 之后将ticket保存到cookie中
		Cookie ticketCookie = new Cookie("JT_TICKET",ticket);
		ticketCookie.setMaxAge(60*60*7*24);
		ticketCookie.setPath("/");
		ticketCookie.setDomain("jt.com");  // 在该域名下实现数据的共享
		response.addCookie(ticketCookie);
		return  SysResult.success();
	}


	/**
	 * 实现用户的退出操作
	 * 1 先查询cookie中的数据
	 * 2 删除redis
	 * 3 删除cookie信息
	 * 4 重定向到系统首页
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		if(cookies == null|| cookies.length == 0){
			return "redirect:/";
		}

		String ticket = null;
		for (Cookie cookie : cookies){
			if("JT_TICKET".equals(cookie.getName())){
				ticket = cookie.getValue();
				break;
			}
		}

		if(StringUtils.isEmpty(ticket)){
			return "redirect:/";
		}

		jedisCluster.del(ticket);

		CookieUtil.deleteCookie("JT_TICKET",response);

		return "redirect:/";
	}


}

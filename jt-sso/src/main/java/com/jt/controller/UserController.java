package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jt.service.UserService;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * 查询user表的全部记录 json展现 findAll
	 */
	@RequestMapping("/findAll")
	public List<User> findAll(){

		return userService.findAll();
	}


	/**
	 * url：http://sso.jt.com/user/check/{param}/{type}
	 * JSONP实现跨域访问：
	 * 	返回值对象：JSONPObject对象（callback，data）
	 * 	业务返回值对象：SysResult
	 * 	页面判断需求：true用户已存在  false可以使用
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
								 @PathVariable Integer type,
								 String callback){

		JSONPObject jsonpObject = null;

		boolean flag = userService.checkUser(param,type);
		jsonpObject = new JSONPObject(callback, SysResult.success(flag));
		return jsonpObject;
//		try {
//			boolean flag = userService.checkUser(param,type);
//			jsonpObject = new JSONPObject(callback, SysResult.success(flag));
//			return jsonpObject;
//		}catch (Exception e){
//			e.printStackTrace();
//			jsonpObject = new JSONPObject(callback,SysResult.fail());
//			return jsonpObject;
//		}

	}

	/**
	 * jsonp跨域调用 根据ticket查询用户信息
	 * 1 根据ticket信息查询redis
	 * 2 根据如果jedis中没有数据，删除cookie记录
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,
										HttpServletResponse response,
										String callback){
		JSONPObject jsonpObject = null;
		String userJson = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJson)){
			CookieUtil.deleteCookie("JT_TICKET",response);
			jsonpObject = new JSONPObject(callback,SysResult.fail());
		}else {
			// 表示用户的json数据成功获取
			SysResult  result = SysResult.success(userJson);
			jsonpObject = new JSONPObject(callback,result);
		}

		return jsonpObject;

	}




}

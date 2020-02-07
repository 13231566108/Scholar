package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import javax.servlet.http.HttpServletRequest;

//拦截Controller层  Controller~~~~Service~~~Mapper
@RestControllerAdvice	//返回值结果都是JSON串
public class SysResultExceptionAOP {

	/**
	 * 统一返回数据SysRusult对象status=201
	 * 业务说明：全局异常处理机制，如果遇到jsonp的跨域访问
	 * 返回值数据应该经过特殊格式封装
	 * callback(SysResult.fail())
	 *
	 * 实现业务思路：
	 * 	1 应该利用request对象 动态获取callback参数
	 * 	2 实现业务特殊封装
	 */
	
	/**
	 * 统一返回数据 SysResult对象 status=201
	 */
	@ExceptionHandler(RuntimeException.class)
	public Object fail(Exception e, HttpServletRequest request) {
		String callback = request.getParameter("callback");
		if(!StringUtils.isEmpty(callback)){
			// 用户发起jsonp的请求
			e.printStackTrace();
			return new JSONPObject(callback,SysResult.fail());
		}
		// 常规调用
		e.printStackTrace();
		return SysResult.fail();
	}
}

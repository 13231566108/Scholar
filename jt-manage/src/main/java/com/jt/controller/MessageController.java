package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	@Value("${server.port}")
	private String port;

	// 动态获取真实服务器端口号
	@RequestMapping("/getPort")
	public String getPort(){
		return port;
	}
}

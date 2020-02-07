package com.jt.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class WebJSONPController {
	
	/**
	 * http://manage.jt.com/web/testJSONP?callback=xxx
	 * 返回值语法规定: callback(json);
	 */
	//@RequestMapping("/web/testJSONP")	
	public String  jsonp(String callback) {
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("JSONP调用成功!!!!");
		String json = ObjectMapperUtil.toJson(itemDesc);
		return callback + "("+json+")";
		
	}
	
	/**
	 * 1.定义返回值结果 JSONPObject
	 * 2.实现数据的封装   参数1:回调函数   参数2:返回数据
	 * @param callback
	 * @return
	 */
	@RequestMapping("/web/testJSONP")	
	public JSONPObject  jsonp2(String callback) {

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("JSONP调用成功!!!!");
	
		return new JSONPObject(callback, itemDesc);
	}
	
}

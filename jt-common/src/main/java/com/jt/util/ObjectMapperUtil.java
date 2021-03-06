package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {


	/**
	 * 1 对象转化为json
	 * 2 json转化为对象
	 */

	public static final ObjectMapper MAPPER = new ObjectMapper();

	public static String toJson(Object obj){

		String result = null;
		try {
			result = MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <T> 定义泛型对象
	 *  T  对象的引用 前后一致即可
	 * @param json
	 * @param target
	 * @param <T>
	 * @return
	 */
	public static <T> T toObj(String json,Class<T> target){
		T t = null;
		try {
			t = MAPPER.readValue(json,target);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return t;
	}
}

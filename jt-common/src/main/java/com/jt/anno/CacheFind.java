package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 该注解对方法有效
@Retention(RetentionPolicy.RUNTIME) // 表示运行期有效
public @interface CacheFind {

	// 保存到redis中key value 用户可以自己指定，也可以动态生成
	public String key() default "";
	public int seconds()default 0; // 设置超时时间 秒
}

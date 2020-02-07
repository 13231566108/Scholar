package com.jt.config;

import com.jt.Interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	@Autowired
	private UserInterceptor interceptor;
	//开启匹配后缀型配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		
		configurer.setUseSuffixPatternMatch(true);
	}

	// 新增拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(interceptor)
				.addPathPatterns("/cart/**","/order/**");
	}
}

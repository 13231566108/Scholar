package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	private String scope;
	public static void deleteCookie(String CookieName,HttpServletResponse response){
		Cookie cookie = new Cookie(CookieName,"");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setDomain("jt.com");// 可以提到配置文件properties
		response.addCookie(cookie);

	}
}

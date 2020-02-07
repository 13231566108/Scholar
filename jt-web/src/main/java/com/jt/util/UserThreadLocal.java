package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocal {
	private static ThreadLocal threadLocal = new ThreadLocal();
	public static void set(User user){
		threadLocal.set(user);
	}

	public static User get(){
		return (User) threadLocal.get();
	}

	public static void remove(){
	threadLocal.remove();
	}
}

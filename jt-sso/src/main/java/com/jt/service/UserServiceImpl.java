package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;


	@Override
	public List<User> findAll() {

		return userMapper.selectList(null);
	}


	/**
	 * true 已存在
	 * false 可以使用
	 * @param param
	 * @param type
	 * @return
	 */
	@Override
	public boolean checkUser(String param, Integer type) {
		String column = null;
		Map<Integer,String> map = new HashMap<>();
		map.put(1,"username");
		map.put(2,"phone");
		map.put(3,"email");
		column = map.get(type);
		QueryWrapper<User> queryWrapper = new QueryWrapper <>();
		queryWrapper.eq(column,param);
		User user = userMapper.selectOne(queryWrapper);
		//int a = 1/0;
		return user == null?false:true;

	}
}

package com.jt.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

@Service
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * 1 密码明文需要加密
	 * 2 需要添加创建时间/更新时间
	 * 3 email为非null，暂时使用phone代替email
	 * @param user
	 */
	@Override
	public void saveUser(User user) {
		String password = user.getPassword();
		String md5Pass = DigestUtils.md5DigestAsHex(password.getBytes());
		user.setId(user.getId()).setEmail(user.getPhone()).setPassword(md5Pass)
				.setUsername(user.getUsername());
		userMapper.insert(user);
	}


	@Override
	public String findUserByUP(User user) {
		// 将密码加密处理
		String md5Pass =
				DigestUtils.md5DigestAsHex(user.getPassword()
						.getBytes());
		user.setPassword(md5Pass);
		// 利用对象中不为null的属性当做where条件
		QueryWrapper<User> queryWrapper = new QueryWrapper <>(user);

		User userDB = userMapper.selectOne(queryWrapper);
		if(userDB == null){
			// 用户名和密码不正确
			return null;
		}
		String ticket = UUID.randomUUID().toString();
		// 防止用户敏感数据泄露，一般会对数据进行脱敏处理
		userDB.setPassword("-_-");
		String json = ObjectMapperUtil.toJson(userDB);
		jedisCluster.setex(ticket,7*24*60*60,json);
		return ticket;
	}
}

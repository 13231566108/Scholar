package com.jt.config;
// 标识配置类信息

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

//	@Value("${redis.sentinel}")
//	private String sentinel;

	@Value("${redis.nodes}")
	private String nodes;

	/**
	 * 使用redis集群，实现redis缓存操作
	 *
	 */
	@Bean
	@Scope("prototype")
	public JedisCluster jedisCluster(){
		Set<HostAndPort> set = new HashSet <>();
		String[] array = nodes.split(",");
		for (String s:array) {
			String host = s.split(":")[0];
			int port = Integer.parseInt(s.split(":")[1]);
			set.add(new HostAndPort(host,port));
		}
		return new JedisCluster(set);
	}
	/**
	 * 使用redis哨兵机制，实现redis缓存操作
	 */

	/*@Bean
	public JedisSentinelPool sentinelPool(){
		Set<String> sentinels = new HashSet <>();
		sentinels.add(sentinel);
		return  new JedisSentinelPool("mymaster",sentinels);
	}*/


	/**
	 * Springboot整合redis jedis
	 */
	/*@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private Integer port;

	@Bean
	@Scope("prototype")
	public Jedis jedis(){
		return new Jedis(host,port);
	}*/

	/**
	 * 配置redis分片功能
	 */
	//@Value("${redis.nodes}")
	//private String nodes;

/*
	@Bean
	public ShardedJedis shardedJedis(){
		List<JedisShardInfo> shards = new ArrayList <>();
		String[] arrays = nodes.split(",");
		for (String node: arrays) {
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			JedisShardInfo info = new JedisShardInfo(host,port);
			shards.add(info);
		}

		return new ShardedJedis(shards);

	}
	*/
}

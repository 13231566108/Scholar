package c.jt.test;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootTest
public class SpringBootTestRedis {
	private Jedis jedis ;


	@BeforeEach
	public void init(){
		jedis = new Jedis("192.168.9.128",6379);
	}
	/*
	redis 入门案例
	host：redis的IP地址
	prot：redis的端口号
	业务：
	 当key存在时，不允许修改数据
	 */
	@Test
	public void testString(){
		Jedis jedis = new Jedis("192.168.9.128",6379);
		jedis.set("gaoxinhuai","da sha bi");
		String value1 = jedis.get("gaoxinhuai");
		System.out.println(value1);
		// set会覆盖之前的数据
		jedis.set("gaoxinhuai","sb");
		System.out.println(jedis.get("gaoxinhuai"));
	}

	@Test
	public void testString2(){
		// 如果key已经存在，则不允许赋值
		jedis.setnx("1909","今天打高新怀");
		jedis.setnx("1909","今天不打高新怀");
		System.out.println(jedis.get("1909"));

	}

	/**
	 * 为数据添加超时时间
	 */
	@Test
	public void testString3() throws InterruptedException {
		jedis.set("eat","随便");
		jedis.expire("eat",10);
		Thread.sleep(2000);
		System.out.println(jedis.ttl("eat"));

		jedis.setex("eat",10,"sss");
		System.out.println(jedis.get("eat"));
	}

	/**
	 * 1 保证数据不被修改
	 * 2 赋值操作与添加超市时间的操作是原子性
	 * 问题 ：nx方法和ex方法不能同时执行
	 * params:
	 * 		nx:不允许覆盖
	 * 		px:毫秒
	 * 		ex:秒
	 */
	@Test
	public void testString4(){
		SetParams setParams = new SetParams();
		setParams.nx().ex(10);
		jedis.set("1909","sss",setParams);

	}

	@Test
	public void testHash(){
		jedis.hset("person","id","200");
		jedis.hset("person","name","tomcat");

		Map<String,String> map = new HashMap <>();
		map.put("id","2000");
		map.put("name","伊朗nb");
		map.put("age","20");
		jedis.hset("student",map);
		System.out.println(jedis.hvals("person"));
		System.out.println(jedis.hgetAll("student"));
	}

	@Test
	public void testList(){
		jedis.lpush("list","1,2,3,4,5");
		System.out.println(jedis.rpop("list")); // 1,2,3,4,5

		jedis.lpush("list","1","2","3","4","5");
		System.out.println(jedis.rpop("list")); // 1
	}

	@Test
	public void testTx(){
		// 开始事务
		Transaction transaction = jedis.multi();
		try {
			// 进行业务操作
			transaction.set("a","aa");
			transaction.set("b","bb");
			transaction.set("c","cc");
			// 提交事务
			transaction.exec();
		}catch (Exception e){
			// 事务的回滚
			transaction.discard();
		}


	}

	@Test
	public void testJedis(){
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.9.128",6379));
		shards.add(new JedisShardInfo("192.168.9.128",6380));
		shards.add(new JedisShardInfo("192.168.9.128",6381));
		ShardedJedis jedis = new ShardedJedis(shards);

		jedis.set("1909","草");
		System.out.println(jedis.get("1909"));
	}

}

package com.jt.aop;

import com.jt.anno.CacheFind;
import com.jt.util.ObjectMapperUtil;
import org.aspectj.lang.ProceedingJoinPoint;


import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.*;

@Component
@Aspect
public class CacheAOP {

	@Autowired(required = false)
	private JedisCluster jedis;
	//private JedisSentinelPool pool;
	//private ShardedJedis jedis;// 引入分片jedis
	//private Jedis jedis;
	/**
	 * 缓存实现业务
	 *  1 从redis中查询数据 获取结果result
	 *  2 result == null 用户第一次查询 ， 执行目标方法，将返回值结果转化为json
	 *    之后保存到redis ，根据seconds数据判断是否超时
	 *  3 result != null 直接将json数据转化为返回值对象
	 * @param joinPoint
	 * @param cacheFind
	 * @return
	 */
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint, CacheFind cacheFind){
		//Jedis jedis = pool.getResource();
		System.out.println("进入aop走缓存");
		String key = getKey(joinPoint,cacheFind);
		String result = jedis.get(key);
		Object obj = null;
		try {
			if(StringUtils.isEmpty(result)){
				obj = joinPoint.proceed();
				result = ObjectMapperUtil.toJson(obj);
				if(cacheFind.seconds()>0)
					jedis.setex(key,cacheFind.seconds(),result);
				else
					jedis.set(key,result);

			}else {
				obj = ObjectMapperUtil.toObj(result,getResultTyep(joinPoint));
		}
		} catch (Throwable throwable) {
			throwable.printStackTrace();

		}
		//jedis.close();
		return obj;

	}

	private Class<?> getResultTyep(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}

	public String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind){
		String key = cacheFind.key();

		if(!StringUtils.isEmpty(key)) return key;

		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		Long arg0 = (Long) joinPoint.getArgs()[0];
		key = className+"."+methodName+"::"+arg0;
		return key;
	}
}

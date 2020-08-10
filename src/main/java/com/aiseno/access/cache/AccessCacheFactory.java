package com.aiseno.access.cache;


import com.aiseno.access.cache.helper.EhCacheHelper;
import com.aiseno.access.cache.helper.ICache;
import com.aiseno.access.cache.helper.RedisHelper;

import redis.clients.jedis.JedisPool;


/**
 * -构造工厂
 * @author VULCAN
 */
public class AccessCacheFactory {
	
	private static ICache cache = null;
	
	private AccessCacheFactory (ICache cache){
		AccessCacheFactory.cache = cache;
	}

	public ICache getInstance(){
		return cache;
	}
	
	public static AccessCacheFactory createDefaultCache(){
		return new AccessCacheFactory(EhCacheHelper.builder());
	}
	
	public static AccessCacheFactory createRedisCache(JedisPool jedisPool){
		return new AccessCacheFactory(new RedisHelper(jedisPool));
	}
}

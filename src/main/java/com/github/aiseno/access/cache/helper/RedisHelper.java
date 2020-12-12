package com.github.aiseno.access.cache.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.aiseno.access.consts.AccessConst;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SuppressWarnings("unchecked")
public class RedisHelper implements ICache {

	private Jedis jedis;
	
	public RedisHelper (JedisPool jedisPool) {
		if(Objects.isNull(jedisPool)) {
			throw new RuntimeException("jedisPool must be not null.");
		}
		this.jedis = jedisPool.getResource();
	}
	
	@Override
	public boolean set(String key, Object value) {
		jedis.set(serialize(key), serialize(value));
		return true;
	}

	@Override
	public boolean set(String key, Object value, int seconds) {
		jedis.setex(serialize(key), seconds, serialize(value));
		return true;
	}

	
	@Override
	public <T> T get(String key) {
		String value = jedis.get(key);
		if(StringUtils.isEmpty(value)) {
			return null;
		}
		return (T) deserialize(value.getBytes());
	}

	@Override
	public <T> List<T> getList(String key) {
		String value = jedis.get(key);
		if(StringUtils.isEmpty(value)) {
			return null;
		}
		return (List<T>) deserialize(value.getBytes());
	}

	@Override
	public int getExpiryTime(String key) {
		return jedis.ttl(key).intValue();
	}

	@Override
	public boolean remove(String... keys) {
		jedis.del(keys);
		return true;
	}

	@Override
	public boolean removeAll() {
		jedis.flushDB();
		return true;
	}

	@Override
	public boolean cleanContext() {
		synchronized (this) {
			Set<String> keys1 = jedis.keys(AccessConst.CACHE_TOKEN_KEY + "*");
			Set<String> keys2 = jedis.keys(AccessConst.CACHE_USER_KEY + "*");
			Set<String> keys3 = jedis.keys(AccessConst.CACHE_TOKEN_EXPIRES_KEY + "*");
			if(!CollectionUtils.isEmpty(keys1)) {
				jedis.del(keys1.toArray(new String[] {}));
			}
			if(!CollectionUtils.isEmpty(keys2)) {
				jedis.del(keys2.toArray(new String[] {}));	
			}
			if(!CollectionUtils.isEmpty(keys3)) {
				jedis.del(keys3.toArray(new String[] {}));
			}
		}
		return true;
	}

	@Override
	public boolean expiry(int seconds, String... keys) {
		if(StringUtils.isEmpty(keys)) {
			return false;
		}
		for(String key : keys) {
		    jedis.expire(key, seconds);
		}
		return true;
	}

	@Override
	public boolean exists(String key) {
		return jedis.exists(key);
	}

	private static byte[] serialize(@Nullable Object object) {
		if (object == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(object);
			oos.flush();
		}catch (IOException ex) {
			throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
		} 
		return baos.toByteArray();
	}

	private static Object deserialize(@Nullable byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			return ois.readObject();
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Failed to deserialize object", ex);
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Failed to deserialize object type", ex);
		}
	}
	
}

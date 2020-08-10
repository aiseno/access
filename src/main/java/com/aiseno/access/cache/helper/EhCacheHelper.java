package com.aiseno.access.cache.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;

@SuppressWarnings("unchecked")
public class EhCacheHelper implements ICache {
	
	private static volatile Cache ehCache;
	
	private static Logger log = LoggerFactory.getLogger(EhCacheHelper.class);
	
	public EhCacheHelper () {
		//双检锁方式创建
		if(ehCache == null) {
			synchronized (EhCacheHelper.class) {
				if(ehCache == null) {
				   ehCache = DefaultCache.create();
				}
			}
		}
		log.info("ehCache hashcode : {} " , ehCache.hashCode());
	}
	
	@Override
	public boolean set(String key , Object value) {
		try {
			ehCache.put(new Element(key,value));
			log.info("set key to ehCache success.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static EhCacheHelper builder() {
		return new EhCacheHelper();
	}
	
	@Override
	public boolean set(String key , Object value,int seconds) {
		try {
			ehCache.put(new Element(key,value,0,seconds));
			log.info("set key to ehCache success.expiry times {} seconds..." , seconds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public <T> T get(String key) {
		try {
			Element element = ehCache.get(key);
			if(element == null) {
				return null;
			}
			return (T) element.getObjectValue();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public <T> List<T> getList(String key){
		try {
			Element element = ehCache.get(key);
			if(element == null) {
				return null;
			}
			return (List<T>) element.getObjectValue();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public int getExpiryTime(String key) {
		Element element = ehCache.get(key);
		if(element == null) {
			return 0;
		}
		long expiry = element.getExpirationTime() - System.currentTimeMillis();
		long time = (expiry / 1000);
		log.info("ehcache key:{} , expiryTime: {} seconds..." , key , time);
		return (int) time;
	}
	
	@Override
	public boolean remove(String...keys) {
		try {
			ehCache.removeAll(Arrays.asList(keys));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean removeContext() {
		return this.removeAll();
	}
	
	@Override
	public boolean removeAll() {
		try {
			ehCache.removeAll();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean expiry(int seconds, String... keys) {
		if(keys == null || keys.length == 0) {
			return false;
		}
		for(String key : keys) {
			Element element = ehCache.get(key);
			if(element == null) {
				return false;
			}
			//重新设置过期时间
			ehCache.put(new Element(key,element.getObjectValue(),0,seconds));
		}
		return true;
	}


	@Override
	public boolean exists(String key) {
		return Objects.nonNull(get(key));
	}

	@SuppressWarnings("deprecation")
	private static class DefaultCache {
		private static Cache ehCache;
		private static final String EHCACHE_NAME = "access_default_ehcache";
		private static final String TMPDIR_PATH = "java.io.tmpdir";
		static {
			Configuration config = new Configuration();
			CacheConfiguration cacheConfiguration = new CacheConfiguration();
			DiskStoreConfiguration diskStoreConfiguration = new DiskStoreConfiguration();
			diskStoreConfiguration.setPath(TMPDIR_PATH);
			cacheConfiguration.setName(EHCACHE_NAME);
			cacheConfiguration.setCopyOnRead(true);
			cacheConfiguration.setEternal(false);//缓存内容是否永久存储在内存；该值设置为true时，timeToIdleSeconds和timeToLiveSeconds两个属性的值就不起作用了。
			cacheConfiguration.setMaxEntriesLocalHeap(2);//设置缓存中允许存放的最大条目数量
			cacheConfiguration.setOverflowToDisk(true);//如果内存中的数据超过maxElementsInMemory，是否使用磁盘存储
			cacheConfiguration.diskPersistent(false);//磁盘存储的条目是否永久保存
			cacheConfiguration.setTimeToIdleSeconds(3600L);//缓存创建以后，最后一次访问缓存的日期至失效之时的时间间隔(秒)
			cacheConfiguration.setDiskExpiryThreadIntervalSeconds(500);//磁盘清理线程的运行时间间隔(秒)
			//缓存名称等
			config.setName(EHCACHE_NAME);
			config.addCache(cacheConfiguration);
			config.addDiskStore(diskStoreConfiguration);
			ehCache = CacheManager.newInstance(config).getCache(EHCACHE_NAME);
		}
		private static Cache create() {
			return ehCache;
		}
	}
}
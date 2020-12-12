package com.github.aiseno.access.listener;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;

import com.github.aiseno.access.cache.helper.ICache;
import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.listener.handle.AccessCustomHandle;
import com.github.aiseno.access.properties.AccessProperties;

public class AccessSessionConfig implements Serializable {
	
	private static final long serialVersionUID = -5727842677773986614L;

	private ICache cache;

	private Integer maxInactiveInterval = AccessConst.CACHE_SESSION_EXPIRE_TIME; //默认 30 分钟

	private AccessCustomHandle accessCustomHandle;
	
	private AccessProperties accessProperties;
	
	private ApplicationContext applicationContext;

	public ICache getCache() {
		return cache;
	}

	public void setCache(ICache cache) {
		this.cache = cache;
	}

	public Integer getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	//单位 秒
	public void setMaxInactiveInterval(Integer maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public AccessCustomHandle getAccessCustomHandle() {
		return accessCustomHandle;
	}

	public void setAccessCustomHandle(AccessCustomHandle accessCustomHandle) {
		this.accessCustomHandle = accessCustomHandle;
	}

	public AccessProperties getAccessProperties() {
		return accessProperties;
	}

	public void setAccessProperties(AccessProperties accessProperties) {
		this.accessProperties = accessProperties;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}

package com.github.aiseno.access.listener;

import org.springframework.context.ApplicationContext;

import com.github.aiseno.access.cache.helper.ICache;
import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.listener.handle.AccessCustomHandle;
import com.github.aiseno.access.listener.handle.AccessHandle;
import com.github.aiseno.access.properties.AccessModeProperties;
import com.github.aiseno.access.properties.AccessProperties;

public class AccessSessionManager {
	
	private ICache cache;

	private Integer maxInactiveInterval = AccessConst.CACHE_SESSION_EXPIRE_TIME; //默认 30 分钟

	private AccessHandle<?> accessHandle;
	
	private AccessCustomHandle accessCustomHandle;
	
	private AccessProperties accessProperties;
	
	private AccessModeProperties modeProperties;
	
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
	public void setMaxInactiveInterval(Integer maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
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
	public AccessHandle<?> getAccessHandle() {
		return accessHandle;
	}
	public void setAccessHandle(AccessHandle<?> accessHandle) {
		this.accessHandle = accessHandle;
	}
	public AccessCustomHandle getAccessCustomHandle() {
		return accessCustomHandle;
	}
	public void setAccessCustomHandle(AccessCustomHandle accessCustomHandle) {
		this.accessCustomHandle = accessCustomHandle;
	}
	public AccessModeProperties getModeProperties() {
		return modeProperties;
	}
	public void setModeProperties(AccessModeProperties modeProperties) {
		this.modeProperties = modeProperties;
	}
}

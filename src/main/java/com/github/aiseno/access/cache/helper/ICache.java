package com.github.aiseno.access.cache.helper;

import java.util.List;

public interface ICache {
	
	public boolean set(String key , Object value);
	
	public boolean set(String key , Object value,int seconds);
	
	public <T> T get(String key);
	
	public <T> List<T> getList(String key);
	
	public int getExpiryTime(String key);
	
	public boolean exists(String key);
	
	public boolean remove(String...keys);
	
	public boolean removeAll();
	
	public boolean expiry(int seconds,String...keys);
	
	public boolean cleanContext();
}

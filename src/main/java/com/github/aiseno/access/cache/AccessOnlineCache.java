package com.github.aiseno.access.cache;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import com.github.aiseno.access.properties.AccessOnline;

public class AccessOnlineCache {
	
	private static final ConcurrentHashMap<String,Object> OVERDUE_MAP = new ConcurrentHashMap<>();

	private AccessOnlineCache () {
		
	}
	
	public static AccessOnline getAccessOnline() {
		AccessOnline online = new AccessOnline();
		online.setLastUpdateTime(new Date());
		online.setOnLineNumber(OVERDUE_MAP.isEmpty() ? 0 : OVERDUE_MAP.keySet().size());
		online.setSessionIdList(OVERDUE_MAP.isEmpty() ? Collections.emptyList() : OVERDUE_MAP.keySet());
		return online;
	}
	
	public static void clean() {
		OVERDUE_MAP.clear();
	}
	
	public static void remove(String key) {
		if(OVERDUE_MAP.get(key) != null) {
		   OVERDUE_MAP.remove(key);
		}
	}
	
	public static void set(String key,Object principal) {
		OVERDUE_MAP.put(key, principal);
	}
}

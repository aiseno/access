package com.aiseno.access.holder;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.aiseno.access.cache.AccessOnlineCache;
import com.aiseno.access.cache.helper.ICache;
import com.aiseno.access.consts.AccessConst;
import com.aiseno.access.properties.AccessOnline;
import com.aiseno.access.properties.AccessPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AccessContextHolder {

	private static final ThreadLocal<ICache> cacheLocal = new ThreadLocal<>();
	
	private static final ThreadLocal<HttpSession> sessionLocal = new ThreadLocal<>();
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static void setServletCacheLocal(ICache cache , HttpSession session) {
		cacheLocal.set(cache);
		sessionLocal.set(session);
	}
	
	public static boolean isAlreadyLogin() {
		return getAccessPrincipal() == null ? false : true;
	}
	
	public static Collection<String> getRoles() {
		AccessPrincipal principal = getAccessPrincipal();
		if(principal == null) {
			return null;
		}
		return principal.getRoles();
	}
	
	public static Collection<String> getPermissions() {
		AccessPrincipal principal = getAccessPrincipal();
		if(principal == null) {
			return null;
		}
		return principal.getPermissions();
	}
	
	public static boolean isAdmin() {
		AccessPrincipal principal = getAccessPrincipal();
		if(principal == null) {
			return false;
		}
		return true;
	}
	
	
	public static AccessOnline getAccessOnline() {
		return AccessOnlineCache.getAccessOnline();
	}
	
	public static AccessPrincipal getAccessPrincipal() {
		HttpSession session = sessionLocal.get();
		ICache cache = cacheLocal.get();
		if(session == null || cache == null) {
		   return null;
		}
		Object token = session.getAttribute(AccessConst.CACHE_TOKEN_KEY);
		if(token == null) {
		   return null;
		}
		Object  o =  cache.get(AccessConst.CACHE_TOKEN_KEY + token.toString());
		if(o == null) {
		   return null;
		}
		try {
			return objectMapper.readValue(objectMapper.writeValueAsBytes(o), AccessPrincipal.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

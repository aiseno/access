package com.github.aiseno.access.holder;

import java.util.Collection;

import com.github.aiseno.access.cache.AccessOnlineCache;
import com.github.aiseno.access.properties.AccessOnline;
import com.github.aiseno.access.properties.AccessPrincipal;

public class AccessContextHolder {
	
	public static Collection<String> getRoles() {
		AccessPrincipal principal = getAccessUser();
		if(principal == null) {
			return null;
		}
		return principal.getRoles();
	}
	
	public static Collection<String> getPermissions() {
		AccessPrincipal principal = getAccessUser();
		if(principal == null) {
			return null;
		}
		return principal.getPermissions();
	}
	
	public static AccessOnline getAccessOnline() {
		return AccessOnlineCache.getAccessOnline();
	}
	
	public static boolean isAdmin() {
		AccessPrincipal principal = getAccessUser();
		if(principal == null) {
			return false;
		}
		return principal.isAdmin();
	}
	
	public static boolean isAlreadyLogin() {
		return getAccessUser() == null ? false : true;
	}
	
	public static AccessPrincipal getAccessPrincipal() {
		return getAccessUser();
	}
	
	//获取用户信息
	private static AccessPrincipal getAccessUser() {
		return AccessContext.doGetAccessPrincipal();
	}

}

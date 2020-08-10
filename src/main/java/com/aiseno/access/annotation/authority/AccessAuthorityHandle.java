package com.aiseno.access.annotation.authority;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.aiseno.access.annotation.AccessPermissions;
import com.aiseno.access.annotation.AccessRoles;
import com.aiseno.access.exception.NotAuthorityAccessException;
import com.aiseno.access.exception.UnauthorizedAccessException;
import com.aiseno.access.holder.AccessContextHolder;
import com.aiseno.access.properties.AccessPrincipal;

public class AccessAuthorityHandle implements IAccessAuthority {

	private static String ROLE_PREFIX = "role#";
	private static String PERMISSION_PREFIX = "permission#";
	
	@Override
	public void handle(Method method) {
		AccessPrincipal principal = AccessContextHolder.getAccessPrincipal();
		if(principal == null) {
		   throw new UnauthorizedAccessException("access principal must be null.");
		}
		if(principal.isAdmin()) {
		   return;
		}
		Collection<String> roleList = principal.getRoles() ,  permissionList = principal.getPermissions();
		if(CollectionUtils.isEmpty(roleList) && CollectionUtils.isEmpty(permissionList)) {
			throw new NotAuthorityAccessException("没有访问权限.");
		}
		AccessRoles roles = method.getAnnotation(AccessRoles.class);
		AccessPermissions permissions = method.getAnnotation(AccessPermissions.class);
		String message = (roles != null) ? roles.message() : (permissions != null) ? permissions.message() : "";
		Map<String,String> identiyMap = getIdentiyMap(roles , permissions);
		if(identiyMap.isEmpty()) {
		   throw new NotAuthorityAccessException("method identiy must be null.");
		}
		this.checks(true, roleList, identiyMap, message);
		this.checks(false, permissionList, identiyMap, message);
	}
	
	protected void checks(boolean isRole ,Collection<String> identiyList , Map<String,String> identiyMap , String message){
		if(CollectionUtils.isEmpty(identiyList)) {
			return;
		}
		String prefix = isRole ? ROLE_PREFIX : PERMISSION_PREFIX;
		boolean yes = false;
		for(String identiy : identiyList) {
			if(identiyMap.get(prefix + identiy) != null) {
				yes = true;
				break;
			};
		}
		if(yes == false) {
		   throw new NotAuthorityAccessException("message.");
		}
	}
	
	
	
	protected Map<String,String> getIdentiyMap(AccessRoles roles , AccessPermissions permissions ){
		Map<String,String> idMap = new HashMap<>();
		if(roles != null) {
			String[] rs = roles.value();
			if(rs != null && rs.length > 0) {
				for(String r : rs) {
					idMap.put(ROLE_PREFIX + r, r);
				}
			}
		}
		if(permissions != null) {
			String[] ps = permissions.value();
			if(ps != null && ps.length > 0) {
				for(String p : ps) {
					idMap.put(PERMISSION_PREFIX + p, p);
				}
			}
		}
		return idMap;
	}
	
	
}

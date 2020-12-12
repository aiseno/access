package com.github.aiseno.access.annotation.authority;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.aiseno.access.annotation.AccessPermissions;
import com.github.aiseno.access.annotation.AccessRoles;
import com.github.aiseno.access.exception.NotAuthorityAccessException;
import com.github.aiseno.access.exception.UnauthorizedAccessException;
import com.github.aiseno.access.holder.AccessContextHolder;
import com.github.aiseno.access.properties.AccessPrincipal;

public class AccessAuthorityHandle implements IAccessAuthority {

	private static final String ROLE_PREFIX = "role#";
	private static final String PERMISSION_PREFIX = "permission#";
	
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
		Map<String,String> identiyMap = getIdentiyMap(roles , permissions);
		if(identiyMap.isEmpty()) {
		   throw new NotAuthorityAccessException("method identiy must be null.");
		}
		checkPermissions(ROLE_PREFIX,  roleList, identiyMap, roles == null ? null : roles.message());
		checkPermissions(PERMISSION_PREFIX, permissionList, identiyMap, permissions == null ? null : permissions.message());
	}
	
	private static void checkPermissions(String prefix ,Collection<String> identiyList , Map<String,String> identiyMap , String message){
		if(CollectionUtils.isEmpty(identiyList)) {
			return;
		}
		boolean yes = false;
		for(String identiy : identiyList) {
			if(identiyMap.get(prefix + identiy) != null) {
				yes = true;
				break;
			};
		}
		if(yes == false) {
		   throw new NotAuthorityAccessException(StringUtils.isEmpty(message) ? "无访问此对象权限." : message);
		}
	}
	
	private static Map<String,String> getIdentiyMap(AccessRoles roles , AccessPermissions permissions){
		Map<String,String> idMap = new HashMap<>();
		if(roles != null && isNotEmpty(roles.value())) {
			for(String role : roles.value()) {
				idMap.put(ROLE_PREFIX + role, role);
			}
		}
		if(permissions != null && isNotEmpty(permissions.value())) {
			for(String permission : permissions.value()) {
			   idMap.put(PERMISSION_PREFIX + permission , permission);
			}
		}
		return idMap;
	}

	private static boolean isNotEmpty(String...array) {
		return (array != null && array.length > 0);
	} 

}

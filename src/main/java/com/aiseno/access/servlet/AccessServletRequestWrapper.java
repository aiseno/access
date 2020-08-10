package com.aiseno.access.servlet;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.CollectionUtils;

import com.aiseno.access.holder.AccessContextHolder;
import com.aiseno.access.properties.AccessPrincipal;

public class AccessServletRequestWrapper extends HttpServletRequestWrapper {
	
	private AccessPrincipal accessPrincipal;
	
	public AccessServletRequestWrapper(HttpServletRequest request) {
		super(request);
		accessPrincipal = AccessContextHolder.getAccessPrincipal();
	}

	@Override
	public Principal getUserPrincipal() {
		return accessPrincipal;
	}

	@Override
	public boolean isUserInRole(String role) {
		if(accessPrincipal == null) {
			return false;
		}
		if(accessPrincipal.isAdmin()) {
			return true;
		}
		Collection<String> roles = accessPrincipal.getRoles();
		if(CollectionUtils.isEmpty(roles)) {
			return false;
		}
		return roles.contains(role);
	}

	
	
	
	
	
	
	
}

package com.github.aiseno.access.servlet.wrapper;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;

import com.github.aiseno.access.holder.AccessContextHolder;
import com.github.aiseno.access.properties.AccessPrincipal;

public class AccessServletWrapper extends HttpServletRequestWrapper {
	
	private AccessPrincipal accessPrincipal;
	
	public AccessServletWrapper(HttpServletRequest request,HttpServletResponse response) {
		super(request);
		this.accessPrincipal = AccessContextHolder.getAccessPrincipal();
		AccessSessionMonitors.createMonitor(request,response,accessPrincipal);
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

	public Serializable getExtras() {
		return accessPrincipal.getExtras();
	}
	
	@Override
	public String getRemoteUser() {
		return accessPrincipal.getName();
	}
	
	@Override
	public void login(String username, String password) throws ServletException {
		
	}

	@Override
	public void logout() throws ServletException {
		
	}

}

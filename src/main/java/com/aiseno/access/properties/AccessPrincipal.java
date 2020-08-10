package com.aiseno.access.properties;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

public class AccessPrincipal implements Principal , Serializable {

	private static final long serialVersionUID = 6524436518934459687L;

	private String sessionId;
	
	private String id;
	
	private String name;
	
	private boolean isAdmin;
	
	private Collection<String> roles;
	
	private Collection<String> permissions;
	
	@Override
	public String getName() {
		return name;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection<String> getRoles() {
		return roles;
	}

	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}

	public Collection<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Collection<String> permissions) {
		this.permissions = permissions;
	}

	public void setName(String name) {
		this.name = name;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}

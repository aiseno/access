package com.github.aiseno.access.properties;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

public class AccessPrincipal implements Principal , Serializable {

	private static final long serialVersionUID = 6524436518934459687L;

	private String tokenId;
	
	private String userId;
	
	private String name;
	
	private boolean isAdmin;
	
	private Collection<String> roles;
	
	private Collection<String> permissions;
	
	private Serializable extras;
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getTokenId() {
		return tokenId;
	}
	
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Serializable getExtras() {
		return extras;
	}

	public void setExtras(Serializable extras) {
		this.extras = extras;
	}
	
}

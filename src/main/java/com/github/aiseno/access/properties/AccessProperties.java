package com.github.aiseno.access.properties;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import com.github.aiseno.access.consts.AccessConst;

public class AccessProperties implements Serializable , AccessConst {

	private static final long serialVersionUID = -5195986031861820061L;
	
	private String loginUrl = DEFAULT_LOGIN_URI;
	
	private String logoutUrl = DEFAULT_LOGOUT_URI;
	
	private String successUrl = DEFAULT_SUCCESS_URI;
	
	private Collection<String> ignoreUris = Collections.emptyList();
	
	private Collection<String> validUris = DEFAULT_VALID_URI;
	
	private AccessModeProperties modeProperties = AccessModeProperties.defaultSessionMode(); //默认session模式

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public Collection<String> getIgnoreUris() {
		return ignoreUris;
	}

	public void setIgnoreUris(Collection<String> ignoreUris) {
		this.ignoreUris = ignoreUris;
	}

	public Collection<String> getValidUris() {
		return validUris;
	}

	public void setValidUris(Collection<String> validUris) {
		this.validUris = validUris;
	}

	public AccessModeProperties getModeProperties() {
		return modeProperties;
	}

	public void setModeProperties(AccessModeProperties modeProperties) {
		this.modeProperties = modeProperties;
	}
}

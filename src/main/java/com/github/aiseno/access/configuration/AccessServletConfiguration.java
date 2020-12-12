package com.github.aiseno.access.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.aiseno.access.annotation.authority.AccessDefaultPointcutAdvisor;
import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.consts.AccessMode;
import com.github.aiseno.access.exception.AccessFilterException;
import com.github.aiseno.access.listener.AccessSessionConfig;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.listener.handle.AccessHandle;
import com.github.aiseno.access.properties.AccessModeProperties;
import com.github.aiseno.access.properties.AccessProperties;
import com.github.aiseno.access.servlet.registration.bean.AccessContextRegistration;

public class AccessServletConfiguration extends AccessContextRegistration {

	private AccessServletConfiguration(AccessSessionManager accessSessionManager) {
		super(accessSessionManager);
		AccessModeProperties modeProperties = accessSessionManager.getModeProperties();
		if(modeProperties == null) {
			throw new AccessFilterException("accessModeProperties object must be not null.");
		}
		if(modeProperties.getMode() == AccessMode.TOKEN) {
			if(modeProperties.isMultipleLogin() && (modeProperties.getTokenPrefix() == null || modeProperties.getTokenPrefix().length == 0)) {
			  throw new AccessFilterException("accessModeProperties tokenPrefix array must be not null.");
			}
		}
		if(StringUtils.isEmpty(modeProperties.getTokenName())) {
			modeProperties.setTokenName(AccessConst.DEFAULT_TOKEN_NAME_KEY);
		}
		if(StringUtils.isEmpty(modeProperties.getTokenAgent())) {
			modeProperties.setTokenAgent(AccessConst.DEFAULT_AGENT_KEY);
		}
	}
	
	public static AccessServletConfiguration create(AccessSessionConfig accessSessionConfig , @Qualifier AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor , @Qualifier AccessHandle<?> accessHandle) {
		return new Builder().create(accessSessionConfig, accessDefaultPointcutAdvisor, accessHandle);
	}
	
	private static class Builder {
		public AccessServletConfiguration create(AccessSessionConfig accessSessionConfig , @Qualifier AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor , @Qualifier AccessHandle<?> accessHandle){
			AccessProperties properties = accessSessionConfig.getAccessProperties();
			if(accessSessionConfig == null || properties == null || accessSessionConfig.getApplicationContext() == null || accessSessionConfig.getCache() == null) {
				throw new AccessFilterException("accessSessionManager attr must be not null.");
			}
			Set<String> ignoreUris = CollectionUtils.isEmpty(properties.getIgnoreUris()) ? new HashSet<>() : new HashSet<>(properties.getIgnoreUris());
			ignoreUris.add("/favicon.ico");
			ignoreUris.add("/error");
			properties.setIgnoreUris(ignoreUris);
			//
			AccessSessionManager accessSessionManager = new AccessSessionManager();
			accessSessionManager.setAccessCustomHandle(accessSessionConfig.getAccessCustomHandle());
			accessSessionManager.setAccessHandle(accessHandle);
			accessSessionManager.setAccessProperties(properties);
			accessSessionManager.setApplicationContext(accessSessionConfig.getApplicationContext());
			accessSessionManager.setCache(accessSessionConfig.getCache());
			accessSessionManager.setMaxInactiveInterval(accessSessionConfig.getMaxInactiveInterval());
			accessSessionManager.setModeProperties(properties.getModeProperties());
			return new AccessServletConfiguration(accessSessionManager);
		}
	}
}

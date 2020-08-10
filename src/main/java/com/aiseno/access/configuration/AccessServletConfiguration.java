package com.aiseno.access.configuration;

import org.springframework.beans.factory.annotation.Qualifier;

import com.aiseno.access.annotation.authority.AccessDefaultPointcutAdvisor;
import com.aiseno.access.exception.AccessFilterException;
import com.aiseno.access.listener.AccessSessionConfig;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.listener.handle.AccessHandle;
import com.aiseno.access.servlet.registration.bean.AccessContextRegistration;

public class AccessServletConfiguration extends AccessContextRegistration {

	private AccessServletConfiguration(AccessSessionManager accessSessionManager) {
		super(accessSessionManager);
	}
	
	public static AccessServletConfiguration create(AccessSessionConfig accessSessionConfig , @Qualifier AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor , @Qualifier AccessHandle<?> accessHandle) {
		return new Builder().create(accessSessionConfig, accessDefaultPointcutAdvisor, accessHandle);
	}
	
	public static class Builder {
		public AccessServletConfiguration create(AccessSessionConfig accessSessionConfig , @Qualifier AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor , @Qualifier AccessHandle<?> accessHandle){
			if(accessSessionConfig == null || accessSessionConfig.getAccessProperties() == null || accessSessionConfig.getApplicationContext() == null || accessSessionConfig.getCache() == null) {
				throw new AccessFilterException("accessSessionManager attr must be not null.");
			}
			AccessSessionManager accessSessionManager = new AccessSessionManager();
			accessSessionManager.setAccessCustomHandle(accessSessionConfig.getAccessCustomHandle());
			accessSessionManager.setAccessHandle(accessHandle);
			accessSessionManager.setAccessProperties(accessSessionConfig.getAccessProperties());
			accessSessionManager.setApplicationContext(accessSessionConfig.getApplicationContext());
			accessSessionManager.setCache(accessSessionConfig.getCache());
			accessSessionManager.setMaxInactiveInterval(accessSessionConfig.getMaxInactiveInterval());
			return new AccessServletConfiguration(accessSessionManager);
		}
	}
}

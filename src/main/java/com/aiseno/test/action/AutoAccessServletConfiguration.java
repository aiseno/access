package com.aiseno.test.action;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aiseno.access.annotation.authority.AccessDefaultPointcutAdvisor;
import com.aiseno.access.cache.AccessCacheFactory;
import com.aiseno.access.configuration.AccessServletConfiguration;
import com.aiseno.access.listener.AccessSessionConfig;
import com.aiseno.access.properties.AccessProperties;

@Configuration
public class AutoAccessServletConfiguration {
	
	private static AccessSessionConfig accessSessionConfig = new AccessSessionConfig();
	
	public AutoAccessServletConfiguration (@Qualifier ApplicationContext applicationContext) {
		accessSessionConfig.setApplicationContext(applicationContext);
	}
	
	static {
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setSuccessUrl("test/hello");
		//
		accessSessionConfig.setCache(AccessCacheFactory.createDefaultCache().getInstance());
		accessSessionConfig.setAccessProperties(accessProperties);
		accessSessionConfig.setAccessCustomHandle(new AccessCustomHandleImpl());
	}
	
	@Bean
	public AccessHandleImpl accessHandleImpl() {
		return new AccessHandleImpl();
	}
	
	@Bean
	public AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor() {
		return new AccessDefaultPointcutAdvisor();
	}
	
	@Bean
	public AccessServletConfiguration accessServletConfiguration() {
		return AccessServletConfiguration.create(accessSessionConfig, this.accessDefaultPointcutAdvisor(), this.accessHandleImpl());
	}
	
}

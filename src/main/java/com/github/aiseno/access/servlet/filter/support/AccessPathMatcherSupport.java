package com.github.aiseno.access.servlet.filter.support;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import com.github.aiseno.access.properties.AccessProperties;

public class AccessPathMatcherSupport {
	
	private static final AntPathMatcher matcher = new AntPathMatcher();
	
	private AccessPathMatcherBuilder accessPathMatcherBuilder;
	
	private AccessProperties accessProperties;
	
	private AccessPathMatcherSupport (AccessPathMatcherBuilder accessPathMatcherBuilder) {
		this.accessPathMatcherBuilder = accessPathMatcherBuilder;
		this.accessProperties = this.accessPathMatcherBuilder.getAccessProperties();
	}
	
	public static AccessPathMatcherBuilder create(AccessProperties accessProperties) {
		return new AccessPathMatcherBuilder(accessProperties);
	}
	
	public boolean isValidLoginUri(String path) {
		String loginUri = accessProperties.getLoginUrl();
		return loginUri == null ? false : isValidUri(Arrays.asList(loginUri), path);
	}
	
	public boolean isValidLogoutUri(String path) {
		String logoutUri = accessProperties.getLogoutUrl();
		return logoutUri == null ? false : isValidUri(Arrays.asList(logoutUri), path);
	}
	
	public boolean isValidIgnoreUri(String path) {
		Collection<String> ignoreUri = accessProperties.getIgnoreUris();
		return isValidUri(ignoreUri, path);
	}
	
	public boolean isSuccessUri(String path) {
		String successUrl = accessProperties.getSuccessUrl();
		return successUrl == null ? false : isValidUri(Arrays.asList(successUrl), path);
	}
	
	public boolean isValidUri(String path) {
		Collection<String> checkUri = accessProperties.getValidUris();
		return isValidUri(checkUri, path);
	}
	
	public boolean isValidUri(String path,String target) {
		return matcher.match(path, target);
	}
	
	public AccessProperties getAccessProperties() {
		return accessPathMatcherBuilder.getAccessProperties();
	}
	
	private boolean isValidUri(Collection<String> uriList , String path) {
		if(CollectionUtils.isEmpty(uriList) || path == null) {
			return false;
		}
		for(String pattern : uriList) {
			boolean isMatcher = matcher.match(pattern, path);
			if(isMatcher) {
				return true;
			}
		}
		return false;
	}
	
	public static class AccessPathMatcherBuilder {
		
		private AccessProperties accessProperties;
		
		private AccessPathMatcherBuilder (AccessProperties accessProperties) {
			this.accessProperties = accessProperties;
		}
		public AccessPathMatcherSupport builder() {
			return new AccessPathMatcherSupport(this);
		}
		public AccessProperties getAccessProperties() {
			return accessProperties;
		}
		public void setAccessProperties(AccessProperties accessProperties) {
			this.accessProperties = accessProperties;
		}
	}
}

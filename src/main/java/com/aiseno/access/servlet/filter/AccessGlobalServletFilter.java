package com.aiseno.access.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiseno.access.cache.helper.ICache;
import com.aiseno.access.exception.AccessFilterException;
import com.aiseno.access.holder.AccessContextHolder;
import com.aiseno.access.holder.ServletHolder;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.listener.handle.AccessCustomHandle;
import com.aiseno.access.servlet.AccessServletRequestWrapper;
import com.aiseno.access.servlet.filter.handle.AccessHandleFilter;
import com.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;

public class AccessGlobalServletFilter implements javax.servlet.Filter {

	private AccessPathMatcherSupport pathMatcherSupport;

	private AccessHandleFilter accessHandleFilter;

	private AccessCustomHandle accessCustomHandle;
	
	private ICache cache;
	
	public AccessGlobalServletFilter(AccessSessionManager accessSessionManager, AccessHandleFilter accessHandleFilter) {
		this.cache = accessSessionManager.getCache();
		this.pathMatcherSupport = AccessPathMatcherSupport.create(accessSessionManager.getAccessProperties()).builder();
		this.accessHandleFilter = accessHandleFilter;
		this.accessCustomHandle = accessSessionManager.getAccessCustomHandle();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String path = httpServletRequest.getServletPath();
		AccessContextHolder.setServletCacheLocal(cache, httpServletRequest.getSession());
		if(pathMatcherSupport.isValidLoginUri(path)) {
			chain.doFilter(request, response);
			return;
		}
		if(accessCustomHandle != null) {
		   accessCustomHandle.doCustomFilter(ServletHolder.getParameter(httpServletRequest),httpServletRequest, httpServletResponse, chain);
		}
		boolean handle = accessHandleFilter.handle( path , pathMatcherSupport);
		if (handle) {
			chain.doFilter(new AccessServletRequestWrapper(httpServletRequest), response);
			return;
		}
		response.flushBuffer();
		throw (AccessFilterException) accessHandleFilter.exception();
	}
}
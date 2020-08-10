package com.aiseno.access.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiseno.access.cache.helper.ICache;
import com.aiseno.access.holder.AccessContextHolder;
import com.aiseno.access.holder.ServletHolder;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.listener.handle.AccessCustomHandle;
import com.aiseno.access.properties.AccessProperties;
import com.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;

public class AccessLoginServletFilter implements javax.servlet.Filter {

	private AccessPathMatcherSupport pathMatcherSupport;

	private AccessProperties accessProperties;
	
	private AccessCustomHandle accessCustomHandle;
	
	private ICache cache;
	
	public AccessLoginServletFilter(AccessSessionManager accessSessionManager) {
		this.cache = accessSessionManager.getCache();
		this.accessProperties = accessSessionManager.getAccessProperties();
		this.accessCustomHandle = accessSessionManager.getAccessCustomHandle();
		this.pathMatcherSupport = AccessPathMatcherSupport.create(accessProperties).builder();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		AccessContextHolder.setServletCacheLocal(cache, httpServletRequest.getSession());
		String path = httpServletRequest.getServletPath();
		boolean login = pathMatcherSupport.isValidLoginUri(path);
		if(login) {
			if(accessCustomHandle != null) {
			   accessCustomHandle.doCustomFilter(ServletHolder.getParameter(httpServletRequest),httpServletRequest, httpServletResponse, chain);
			}
			String successUrl = accessProperties.getSuccessUrl();
			if (AccessContextHolder.isAlreadyLogin()) {
				String serverPath = httpServletRequest.getRequestURL().toString().replace(path,"");
				if(!serverPath.endsWith("/") && !successUrl.startsWith("/")) {
					serverPath = serverPath + "/";
				}
				String redirect = serverPath + successUrl;
				httpServletResponse.sendRedirect(redirect);
				return;
			}
		}
		chain.doFilter(request, response);
	}
}
package com.github.aiseno.access.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.aiseno.access.consts.AuthorizedConst;
import com.github.aiseno.access.exception.CustomAccessException;
import com.github.aiseno.access.holder.AccessContextHolder;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.listener.handle.AccessCustomHandle;
import com.github.aiseno.access.properties.AccessProperties;
import com.github.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;

public class AccessLoginServletFilter implements javax.servlet.Filter {

	private AccessPathMatcherSupport pathMatcherSupport;

	private AccessProperties accessProperties;
	
	private AccessCustomHandle accessCustomHandle;
	
	public AccessLoginServletFilter(AccessSessionManager accessSessionManager) {
		this.accessProperties = accessSessionManager.getAccessProperties();
		this.accessCustomHandle = accessSessionManager.getAccessCustomHandle();
		this.pathMatcherSupport = AccessPathMatcherSupport.create(accessProperties).builder();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		final String path = httpServletRequest.getServletPath();
		boolean login = pathMatcherSupport.isValidLoginUri(path);
		if(login) {
			if(accessCustomHandle != null) {
			   boolean dos = accessCustomHandle.preHandle(httpServletRequest);
			   if(dos == false) {
				   httpServletRequest.setAttribute(AuthorizedConst.EXCEPTION_ATTR_NAME, new CustomAccessException(path,"preHandle invalid verification."));
				   httpServletRequest.getRequestDispatcher(AuthorizedConst.EXCEPTION_MOTHED_NAME).forward(httpServletRequest, httpServletResponse);
				   return; 
			   }
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
		chain.doFilter(httpServletRequest, httpServletResponse);
	}
}
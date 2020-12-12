package com.github.aiseno.access.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.aiseno.access.consts.AuthorizedConst;
import com.github.aiseno.access.exception.AccessFilterException;
import com.github.aiseno.access.exception.CustomAccessException;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.listener.handle.AccessCustomHandle;
import com.github.aiseno.access.servlet.filter.handle.AccessHandleFilter;
import com.github.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;
import com.github.aiseno.access.servlet.wrapper.AccessServletWrapper;

public class AccessGlobalServletFilter implements javax.servlet.Filter {

	private final AccessPathMatcherSupport pathMatcherSupport;

	private final AccessHandleFilter accessHandleFilter;

	private final AccessCustomHandle accessCustomHandle;
	
	public AccessGlobalServletFilter(AccessSessionManager accessSessionManager, AccessHandleFilter accessHandleFilter) {
		this.pathMatcherSupport = AccessPathMatcherSupport.create(accessSessionManager.getAccessProperties()).builder();
		this.accessHandleFilter = accessHandleFilter;
		this.accessCustomHandle = accessSessionManager.getAccessCustomHandle();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		final String path = httpServletRequest.getServletPath();
		//认证异常
		if(pathMatcherSupport.isValidUri(AuthorizedConst.EXCEPTION_MOTHED_NAME, path)) {
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		};
		//是否是登录接口
		if(pathMatcherSupport.isValidLoginUri(path)) {
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		//前置自定义校验
		if(accessCustomHandle != null) {
		   boolean dos = accessCustomHandle.preHandle(httpServletRequest);
		   if(dos == false) {
			   httpServletRequest.setAttribute(AuthorizedConst.EXCEPTION_ATTR_NAME, new CustomAccessException(path,"preHandle Invalid verification."));
			   httpServletRequest.getRequestDispatcher(AuthorizedConst.EXCEPTION_MOTHED_NAME).forward(httpServletRequest, httpServletResponse);
			   return; 
		   }
		}
		boolean handle = accessHandleFilter.handle(path,pathMatcherSupport);
		if (handle) {
			chain.doFilter(new AccessServletWrapper(httpServletRequest,httpServletResponse), httpServletResponse);
			return;
		}
		AccessFilterException exception = (AccessFilterException) accessHandleFilter.exception();
		httpServletRequest.setAttribute(AuthorizedConst.EXCEPTION_ATTR_NAME, exception);
		httpServletRequest.getRequestDispatcher(AuthorizedConst.EXCEPTION_MOTHED_NAME).forward(httpServletRequest, httpServletResponse);
	}
}
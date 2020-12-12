package com.github.aiseno.access.servlet.registration.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.aiseno.access.holder.ServletHolder;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.servlet.filter.AccessGlobalServletFilter;
import com.github.aiseno.access.servlet.filter.handle.AccessHandleFilterImpl;


public class AccessGlobalFilterRegistrationBean extends FilterRegistrationBean<AccessGlobalServletFilter>{

	private static final Integer ORDER = Integer.MIN_VALUE + 999;
	
	private static final String NAME = "accessServletFilter";
	
	private static final String[] URL_MAPPINGS = new String[] { "/**" };
	
	private static AccessHandleFilterImpl accessHandleFilterImpl = new AccessHandleFilterImpl();
	
	public AccessGlobalFilterRegistrationBean(AccessSessionManager manager) {
		AccessGlobalServletFilter accessServletFilter = new AccessGlobalServletFilter(manager,accessHandleFilterImpl);
		ServletHolder.registerBean((ConfigurableApplicationContext) manager.getApplicationContext(), NAME, accessServletFilter);
		super.setFilter(accessServletFilter);
		super.setEnabled(true);
		super.addUrlPatterns(URL_MAPPINGS);
		super.setName(NAME);
		super.setOrder(ORDER);
	}
}

package com.aiseno.access.servlet.registration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;

import com.aiseno.access.holder.ServletHolder;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.servlet.filter.AccessGlobalServletFilter;
import com.aiseno.access.servlet.filter.handle.AccessHandleFilterImpl;


@Deprecated
public class AccessFilterRegistrationBean extends FilterRegistrationBean<AccessGlobalServletFilter>{

	private static final Integer ORDER = Integer.MIN_VALUE + 999;
	
	private static final String NAME = "accessServletFilter";
	
	private static final String[] URL_MAPPINGS = new String[] { "/*" };
	
	private static AccessHandleFilterImpl accessHandleFilterImpl = new AccessHandleFilterImpl();
	
	public AccessFilterRegistrationBean(AccessSessionManager manager) {
		AccessGlobalServletFilter accessServletFilter = new AccessGlobalServletFilter(manager,accessHandleFilterImpl);
		ServletHolder.registerBean((ConfigurableApplicationContext) manager.getApplicationContext(), NAME, accessServletFilter);
		super.setFilter(accessServletFilter);
		super.setEnabled(true);
		super.addUrlPatterns(URL_MAPPINGS);
		super.setName(NAME);
		super.setOrder(ORDER);
	}
}

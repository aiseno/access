package com.aiseno.access.servlet.registration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;

import com.aiseno.access.holder.ServletHolder;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.servlet.filter.AccessLoginServletFilter;

@Deprecated
public class AccessLoginFilterRegistrationBean extends FilterRegistrationBean<AccessLoginServletFilter>{

	private static final Integer ORDER = Integer.MIN_VALUE + 888;
	
	private static final String NAME = "accessLoginServletFilter";
	
	private static final String[] URL_MAPPINGS = new String[] { "/*" , "/**"};
	
	public AccessLoginFilterRegistrationBean(AccessSessionManager manager) {
		AccessLoginServletFilter accessServletFilter = new AccessLoginServletFilter(manager);
		ServletHolder.registerBean((ConfigurableApplicationContext) manager.getApplicationContext(), NAME, accessServletFilter);
		super.setFilter(accessServletFilter);
		super.setEnabled(true);
		super.addUrlPatterns(URL_MAPPINGS);
		super.setName(NAME);
		super.setOrder(ORDER);
	}
	
}

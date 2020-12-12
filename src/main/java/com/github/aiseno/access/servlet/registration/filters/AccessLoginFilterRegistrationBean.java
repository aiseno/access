package com.github.aiseno.access.servlet.registration.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.holder.ServletHolder;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.servlet.filter.AccessLoginServletFilter;

public class AccessLoginFilterRegistrationBean extends FilterRegistrationBean<AccessLoginServletFilter>{

	private static final Integer ORDER = Integer.MIN_VALUE + 888;
	
	private static final String NAME = "accessLoginServletFilter";
	
	private static final String[] URL_MAPPINGS = AccessConst.DEFAULT_VALID_URI.toArray(new String[] {});
	
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

package com.github.aiseno.access.servlet.registration.servlets;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.holder.ServletHolder;
import com.github.aiseno.access.listener.handle.AccessHandleApplicationListener;
import com.github.aiseno.access.servlet.AccessHandleServlet;

public class AccessServletRegistrationBean extends ServletRegistrationBean<AccessHandleServlet> {

	private static final Integer ORDER = Integer.MIN_VALUE + 1000;
	
	private static final String HANDLE_LISTENER_NAME = "accessHandleListener";
	
	private static final String HANDLE_SERVLET_NAME = "accessHandleServlet";
	
	private static final String[] URL_MAPPINGS = new String[] {AccessConst.DEFAULT_LOGIN_URI,AccessConst.DEFAULT_LOGOUT_URI};
	
	public AccessServletRegistrationBean(ApplicationContext applicationContext) {
		final ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
		final AccessHandleServlet accessHandleServlet =  new AccessHandleServlet(applicationContext);
		ServletHolder.registerBean(context,HANDLE_SERVLET_NAME, accessHandleServlet);
		ServletHolder.registerBean(context,HANDLE_LISTENER_NAME, new AccessHandleApplicationListener());
		super.setServlet(accessHandleServlet); 
		super.setOrder(ORDER);
		super.addUrlMappings(URL_MAPPINGS);
		super.setName(HANDLE_SERVLET_NAME);
		super.setEnabled(true);
	}

	
	
}

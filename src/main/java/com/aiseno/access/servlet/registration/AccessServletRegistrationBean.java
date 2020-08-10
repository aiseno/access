package com.aiseno.access.servlet.registration;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;

import com.aiseno.access.holder.ServletHolder;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.listener.session.AccessHandleListener;
import com.aiseno.access.servlet.AccessHandleServlet;

@Deprecated
public class AccessServletRegistrationBean extends ServletRegistrationBean<AccessHandleServlet> {

	private static final Integer ORDER = Integer.MIN_VALUE + 1000;
	
	private static final String HANDLE_LISTENER_NAME = "accessHandleListener";
	
	private static final String HANDLE_SERVLET_NAME = "accessHandleServlet";
	
	private static final String[] URL_MAPPINGS = new String[] { "/access/login/v1" , "/access/logout/v1" };
	
	public AccessServletRegistrationBean(AccessSessionManager manager) {
		final ConfigurableApplicationContext context = (ConfigurableApplicationContext) manager.getApplicationContext();
		final AccessHandleServlet accessHandleServlet =  new AccessHandleServlet(manager);
		ServletHolder.registerBean(context,HANDLE_SERVLET_NAME, accessHandleServlet);
		ServletHolder.registerBean(context,HANDLE_LISTENER_NAME, new AccessHandleListener());
		super.setServlet(accessHandleServlet); 
		super.setOrder(ORDER);
		super.addUrlMappings(URL_MAPPINGS);
		super.setName(HANDLE_SERVLET_NAME);
		super.setEnabled(true);
	}

	
	
}

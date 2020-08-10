package com.aiseno.access.servlet.registration.bean;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import com.aiseno.access.exception.AccessFilterException;
import com.aiseno.access.holder.ServletHolder;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.listener.session.AccessContextListener;
import com.aiseno.access.listener.session.AccessHandleListener;
import com.aiseno.access.listener.session.AccessSessionListener;
import com.aiseno.access.servlet.AccessHandleServlet;
import com.aiseno.access.servlet.filter.AccessGlobalServletFilter;
import com.aiseno.access.servlet.filter.AccessLoginServletFilter;
import com.aiseno.access.servlet.filter.handle.AccessHandleFilterImpl;

public class AccessContextRegistration implements ServletContextInitializer, Ordered {

	private AccessSessionManager accessSessionManager; 

	private static EnumSet<DispatcherType> enumSet = EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,DispatcherType.ASYNC,DispatcherType.ERROR);
	
	public AccessContextRegistration(AccessSessionManager accessSessionManager) {
		if(accessSessionManager == null || accessSessionManager.getAccessProperties() == null || accessSessionManager.getApplicationContext() == null || accessSessionManager.getCache() == null || accessSessionManager.getAccessHandle() == null) {
			throw new AccessFilterException("accessSessionManager attr must be not null.");
		}
		this.accessSessionManager = accessSessionManager;
	}

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		new AccessServletRegistration(accessSessionManager, servletContext);
		new AccessLoginFilterRegistration(accessSessionManager, servletContext);
		new AccessFilterRegistration(accessSessionManager, servletContext);
		//
		servletContext.addListener(new AccessSessionListener(accessSessionManager));
		servletContext.addListener(new AccessContextListener(accessSessionManager));
	}

	private static class AccessServletRegistration {

		private static final Integer ORDER = Integer.MIN_VALUE + 1000;

		private static final String HANDLE_LISTENER_NAME = "accessHandleListener";

		private static final String HANDLE_SERVLET_NAME = "accessHandleServlet";

		private static final String[] URL_MAPPINGS = new String[] { "/access/login/v1", "/access/logout/v1" };

		public AccessServletRegistration(AccessSessionManager manager, ServletContext servletContext) {
			Dynamic dynamic = servletContext.addServlet(HANDLE_SERVLET_NAME, new AccessHandleServlet(manager));
			dynamic.addMapping(URL_MAPPINGS);
			dynamic.setLoadOnStartup(ORDER);
			ServletHolder.registerBean((ConfigurableApplicationContext) manager.getApplicationContext(), HANDLE_LISTENER_NAME, new AccessHandleListener());
		}
	}

	private static class AccessLoginFilterRegistration {

		private static final String FILTER_NAME = "accessLoginServletFilter";

		private static final String[] URL_MAPPINGS = new String[] { "/access/login/v1", "/access/logout/v1" };

		public AccessLoginFilterRegistration(AccessSessionManager manager, ServletContext servletContext) {
			FilterRegistration.Dynamic dynamic = servletContext.addFilter(FILTER_NAME,new AccessLoginServletFilter(manager));
			dynamic.addMappingForUrlPatterns(enumSet, false, URL_MAPPINGS);
			dynamic.setAsyncSupported(true);
		}
	}

	private static class AccessFilterRegistration {
		
		private static final String FILTER_NAME = "accessServletFilter";
		
		private static final String[] URL_MAPPINGS = new String[] { "/*" , "/**"};
		
		private static AccessHandleFilterImpl accessHandleFilterImpl = new AccessHandleFilterImpl();

		public AccessFilterRegistration(AccessSessionManager manager, ServletContext servletContext) {
			FilterRegistration.Dynamic dynamic = servletContext.addFilter(FILTER_NAME,new AccessGlobalServletFilter(manager, accessHandleFilterImpl));
			dynamic.addMappingForUrlPatterns(enumSet, false, URL_MAPPINGS);
			dynamic.setAsyncSupported(true);
		}
	}
}

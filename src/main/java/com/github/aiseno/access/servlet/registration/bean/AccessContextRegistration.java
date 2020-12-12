package com.github.aiseno.access.servlet.registration.bean;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.exception.AccessFilterException;
import com.github.aiseno.access.holder.AccessContext;
import com.github.aiseno.access.holder.ServletHolder;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.listener.handle.AccessHandleApplicationListener;
import com.github.aiseno.access.listener.session.AccessContextListener;
import com.github.aiseno.access.listener.session.AccessRequestListener;
import com.github.aiseno.access.listener.session.AccessSessionListener;
import com.github.aiseno.access.servlet.AccessHandleServlet;
import com.github.aiseno.access.servlet.action.AccessDefaultAction;
import com.github.aiseno.access.servlet.filter.AccessGlobalServletFilter;
import com.github.aiseno.access.servlet.filter.AccessLoginServletFilter;
import com.github.aiseno.access.servlet.filter.handle.AccessHandleFilterImpl;

public class AccessContextRegistration implements ServletContextInitializer, Ordered {

	private static Logger logger = LoggerFactory.getLogger(AccessContextRegistration.class);
	
	private final AccessSessionManager accessSessionManager; 
	
	private final Integer maxInactiveInterval; //秒

	private final static EnumSet<DispatcherType> enumSet = EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,DispatcherType.ASYNC,DispatcherType.ERROR,DispatcherType.INCLUDE);
	
	public AccessContextRegistration(AccessSessionManager accessSessionManager) {
		if(accessSessionManager == null || accessSessionManager.getAccessProperties() == null || accessSessionManager.getApplicationContext() == null || accessSessionManager.getCache() == null || accessSessionManager.getAccessHandle() == null) {
			throw new AccessFilterException("accessSessionManager attr must be not null.");
		}
		this.accessSessionManager = accessSessionManager;
		this.maxInactiveInterval = accessSessionManager.getMaxInactiveInterval();
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AccessContext.builder(accessSessionManager,servletContext.getSessionCookieConfig());
		new AccessServletRegistration(accessSessionManager.getApplicationContext(), servletContext);
		new AccessLoginFilterRegistration(this.accessSessionManager, servletContext);
		new AccessFilterRegistration(this.accessSessionManager, servletContext);
		//cookie 设置
		SessionCookieConfig cookieConfig = servletContext.getSessionCookieConfig();
		cookieConfig.setMaxAge(maxInactiveInterval);
		//监听
		servletContext.addListener(new AccessContextListener(accessSessionManager));
		servletContext.addListener(new AccessSessionListener(accessSessionManager));
		servletContext.addListener(new AccessRequestListener());
		servletContext.setRequestCharacterEncoding("UTF-8");
		servletContext.setResponseCharacterEncoding("UTF-8");
		logger.info("setting accessMode = {}",accessSessionManager.getModeProperties().getMode());
	}

	
	@Override
	public int getOrder() {
		return -1;
	}
	
	/**
	 * -注册登录Servlet
	 */
	private static class AccessServletRegistration {

		private static final Integer ORDER = Integer.MIN_VALUE + 1000;

		private static final String HANDLE_LISTENER_NAME = "accessHandleListener";
		private static final String HANDLE_ACTION_NAME   = "accessDefaultAction";
		private static final String HANDLE_SERVLET_NAME  = "accessHandleServlet";

		private static final String[] URL_MAPPINGS = new String[] {AccessConst.DEFAULT_LOGIN_URI , AccessConst.DEFAULT_LOGOUT_URI};

		public AccessServletRegistration(ApplicationContext applicationContext, ServletContext servletContext) {
			Dynamic dynamic = servletContext.addServlet(HANDLE_SERVLET_NAME, new AccessHandleServlet(applicationContext));
			dynamic.addMapping(URL_MAPPINGS);
			dynamic.setLoadOnStartup(ORDER);
			ServletHolder.registerBean((ConfigurableApplicationContext) applicationContext , HANDLE_LISTENER_NAME, new AccessHandleApplicationListener());
			ServletHolder.registerBean((ConfigurableApplicationContext) applicationContext , HANDLE_ACTION_NAME, new AccessDefaultAction());
		}
	}

	/**
	 * -注册登录过滤器
	 */
	private static class AccessLoginFilterRegistration {

		private static final String FILTER_NAME = "accessLoginServletFilter";

		private static final String[] URL_MAPPINGS = new String[] {AccessConst.DEFAULT_LOGIN_URI , AccessConst.DEFAULT_LOGOUT_URI};

		public AccessLoginFilterRegistration(AccessSessionManager manager, ServletContext servletContext) {
			FilterRegistration.Dynamic dynamic = servletContext.addFilter(FILTER_NAME,new AccessLoginServletFilter(manager));
			dynamic.addMappingForUrlPatterns(enumSet, false, URL_MAPPINGS);
			dynamic.setAsyncSupported(true);
		}
	}

	/**
	 * -注册全局过滤器
	 */
	private static class AccessFilterRegistration {
		
		private static final String FILTER_NAME = "accessServletFilter";
		
		private static AccessHandleFilterImpl accessHandleFilterImpl = new AccessHandleFilterImpl();

		public AccessFilterRegistration(AccessSessionManager manager, ServletContext servletContext) {
			FilterRegistration.Dynamic dynamic = servletContext.addFilter(FILTER_NAME,new AccessGlobalServletFilter(manager, accessHandleFilterImpl));
			dynamic.addMappingForUrlPatterns(enumSet, false, AccessConst.DEFAULT_VALID_URI.toArray(new String[] {}));
			dynamic.setAsyncSupported(true);
		}
	}

}

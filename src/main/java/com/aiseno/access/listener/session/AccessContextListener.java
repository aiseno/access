package com.aiseno.access.listener.session;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.aiseno.access.cache.AccessOnlineCache;
import com.aiseno.access.cache.helper.ICache;
import com.aiseno.access.listener.AccessSessionManager;

public class AccessContextListener implements ServletContextListener {

	private ICache cacheService;
	
	public AccessContextListener (AccessSessionManager accessSessionManager) {
		this.cacheService = accessSessionManager.getCache();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		this.cleanupAttributes(sce.getServletContext());
	}
	
	private void cleanupAttributes(ServletContext servletContext) {
		Enumeration<String> attrNames = servletContext.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			   servletContext.removeAttribute(attrNames.nextElement());
		}
		cacheService.removeContext();
		AccessOnlineCache.clean();
	}
	
}

package com.github.aiseno.access.listener.session;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aiseno.access.cache.AccessOnlineCache;
import com.github.aiseno.access.cache.helper.ICache;
import com.github.aiseno.access.listener.AccessSessionManager;

public class AccessContextListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(AccessContextListener.class);
	
	private final ICache cacheService;
	
	public AccessContextListener (AccessSessionManager accessSessionManager) {
		this.cacheService = accessSessionManager.getCache();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		this.cleanServletContextAttributes(sce.getServletContext());
		cacheService.cleanContext();
		AccessOnlineCache.clean();
		logger.info("contextDestroyed success.");
	}
	
	private void cleanServletContextAttributes(ServletContext servletContext) {
		try {
			Enumeration<String> attrNames = servletContext.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String attrName = attrNames.nextElement();
				if(attrName != null) {
				   servletContext.removeAttribute(attrName);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}

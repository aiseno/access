package com.github.aiseno.access.listener.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aiseno.access.cache.AccessOnlineCache;
import com.github.aiseno.access.cache.helper.ICache;
import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.consts.AccessMode;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.properties.AccessModeProperties;

public class AccessSessionListener implements HttpSessionListener , HttpSessionAttributeListener {
	
	private static Logger logger = LoggerFactory.getLogger(AccessContextListener.class);
	
	private final AccessSessionManager accessSessionManager;
	
	private final ICache cacheService;
	
	private final AccessModeProperties mode;
	
	public AccessSessionListener (AccessSessionManager accessSessionManager) {
		this.accessSessionManager = accessSessionManager;
		this.mode = accessSessionManager.getModeProperties();
		this.cacheService = accessSessionManager.getCache();
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		if(this.mode.getMode() == AccessMode.TOKEN) {
			session.setMaxInactiveInterval(-1);
		} else {
			session.setMaxInactiveInterval(accessSessionManager.getMaxInactiveInterval());
		}
		logger.info("sessionCreated , sessionId = {} , mode = {} " , session.getId() , mode.getMode());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		this.doDestroyed(se.getSession());
	}
	
	private void doDestroyed(HttpSession session) {
		try {
			Object token = session.getAttribute(AccessConst.CACHE_TOKEN_KEY);
			if(token != null) {
			   String tokenId = token.toString();
			   cacheService.remove(AccessConst.CACHE_TOKEN_KEY + tokenId);
			   cacheService.remove(AccessConst.CACHE_USER_KEY + tokenId);
			   cacheService.remove(AccessConst.CACHE_TOKEN_EXPIRES_KEY + tokenId);
			   AccessOnlineCache.remove(tokenId);
			}
			session.removeAttribute(AccessConst.CACHE_TOKEN_KEY);
			session.removeAttribute(AccessConst.CACHE_TOKEN_EXPIRES_KEY);
			logger.info("sessionDestroyed , sessionId = {} , tokenId = {} " , session.getId() , token);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}

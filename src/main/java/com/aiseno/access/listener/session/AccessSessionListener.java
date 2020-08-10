package com.aiseno.access.listener.session;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.aiseno.access.cache.AccessOnlineCache;
import com.aiseno.access.cache.helper.ICache;
import com.aiseno.access.consts.AccessConst;
import com.aiseno.access.listener.AccessSessionManager;

public class AccessSessionListener implements HttpSessionListener , HttpSessionAttributeListener , ServletRequestListener {
	
	
	private static ThreadLocal<HttpSession> sessionLocal = new ThreadLocal<>();
	
	private AccessSessionManager accessSessionManager;
	
	private ICache cacheService;
	
	public AccessSessionListener (AccessSessionManager accessSessionManager) {
		this.accessSessionManager = accessSessionManager;
		this.cacheService = accessSessionManager.getCache();
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSessionListener.super.sessionCreated(se);
		HttpSession session = se.getSession();
		session.setMaxInactiveInterval(accessSessionManager.getMaxInactiveInterval());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession httpSession = se.getSession();
		Object token = httpSession.getAttribute(AccessConst.CACHE_TOKEN_KEY);
		if(token != null) {
		   cacheService.remove(AccessConst.CACHE_TOKEN_KEY + token.toString());
		   AccessOnlineCache.remove(token.toString());
		}
		sessionLocal.remove();
		httpSession.removeAttribute(AccessConst.CACHE_TOKEN_KEY);
		httpSession.removeAttribute(AccessConst.CACHE_TOKEN_EXPIRES_KEY);
		httpSession.invalidate();
	}
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {
		HttpSession session = se.getSession();
		Object token = session.getAttribute(AccessConst.CACHE_TOKEN_KEY);
		if(token != null) {
		   AccessOnlineCache.set(token.toString(), cacheService.get(AccessConst.CACHE_TOKEN_KEY + token.toString()));
		}
	}
	
	@Override
	public void requestInitialized(ServletRequestEvent requestEvent) {
		if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
			throw new IllegalArgumentException("Request is not an HttpServletRequest: " + requestEvent.getServletRequest());
		}
		HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
		HttpSession session = request.getSession();
		Object token = session.getAttribute(AccessConst.CACHE_TOKEN_KEY);
		if(token != null) {
		   Object userObject = cacheService.get(AccessConst.CACHE_TOKEN_KEY + token.toString());
		   if(userObject != null) {
			  request.setAttribute(AccessConst.CACHE_TOKEN_KEY, token);
			  request.setAttribute(AccessConst.CACHE_TOKEN_KEY + token.toString(), userObject);
			  AccessOnlineCache.set(token.toString().toString(),userObject);
		   }
		}
		sessionLocal.set(session);
	}
	
	@Override
	public void requestDestroyed(ServletRequestEvent requestEvent) {
		HttpSession session = sessionLocal.get();
		if(session == null) {
			return;
		}
		Object expireTimeObject = session.getAttribute(AccessConst.CACHE_TOKEN_EXPIRES_KEY);
		Object tokenObject = session.getAttribute(AccessConst.CACHE_TOKEN_KEY);
		if(expireTimeObject != null && tokenObject != null) {
			long lastAccessedTime = session.getLastAccessedTime();
			//最后一次活动时间距离过期时间五分钟内过期,重置过期时间 + 30分钟
			if(Long.valueOf(expireTimeObject.toString()) - lastAccessedTime < 300000 ) {
				synchronized (this) {
					cacheService.expiry(1800,AccessConst.CACHE_TOKEN_KEY +  tokenObject.toString()); //自动续期三十分钟
					session.setAttribute(AccessConst.CACHE_TOKEN_EXPIRES_KEY, System.currentTimeMillis() + 1800000 );
				}
			}
		}
	}
	
}

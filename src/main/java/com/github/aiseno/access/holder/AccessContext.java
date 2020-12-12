package com.github.aiseno.access.holder;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.github.aiseno.access.cache.helper.ICache;
import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.listener.session.impl.AccessDefault;
import com.github.aiseno.access.properties.AccessModeProperties;
import com.github.aiseno.access.properties.AccessPrincipal;
import com.github.aiseno.access.util.DateKti;

public abstract class AccessContext implements AccessDefault {
	
	private static Logger logger = LoggerFactory.getLogger(AccessContext.class);
	
	private static final ThreadLocal<HttpServletRequest> requestLocal = new InheritableThreadLocal<>();
	
	private static volatile AccessModeProperties modeProperties;
	
	private static volatile ICache cacheService;
	
	private static volatile AccessSessionManager accessSessionManager;
	
	private static volatile SessionCookieConfig cookieConfig;
	
	public static void setRequestLocal(HttpServletRequest request) {
		requestLocal.set(request);
	}
	
	public static void clears() {
		requestLocal.remove();
	}
	
	public static ICache getCacheService() {
		return cacheService;
	}
	
	public static AccessSessionManager getAccessManager() {
		return accessSessionManager;
	}
	
	public static SessionCookieConfig getCookieConfig() {
		return cookieConfig;
	}
	
	public static HttpServletRequest getLocalRequest() {
		return requestLocal.get();
	}
	
	public static String getTokenId() {
		return doGetTokenId();
	}
	
	//获取token
	private static String doGetTokenId() {
		switch (modeProperties.getMode()) {
			case SESSION:
				return getTokenIdByModeSession();
			case TOKEN:
				return getTokenIdByModeToken();
		}
		return null;
	}
	
	public static AccessPrincipal doGetAccessPrincipal() {
		String tokenId = getTokenId();
		if(tokenId == null) {
		   return null;
		} 
		switch (modeProperties.getMode()) {
			case SESSION:
				return cacheService.get(String.format(SESSION_MODE_CACHE_USER_INFO, tokenId));
			case TOKEN:
				String agent = ServletHolder.getMultipleAgent(requestLocal.get(), modeProperties.getTokenAgent());
				return cacheService.get(String.format(TOKEN_MODE_CACHE_USER_INFO,agent,tokenId));
		}
		return null;
	}
	
	//session 模式
	private static String getTokenIdByModeSession() {
		HttpServletRequest httpServletRequest = requestLocal.get();
		if(httpServletRequest == null) {
		   return null;
		}
		final String tokenName = modeProperties.getTokenName();
		Object token = httpServletRequest.getAttribute(tokenName);
		if(token == null) {
		   token = httpServletRequest.getSession().getAttribute(tokenName);
		}
		if(token != null) {
		   return token.toString();
		}
		Cookie[] cookies = requestLocal.get().getCookies();
		if(cookies == null || cookies.length == 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(AccessConst.DEFAULT_TOKEN_NAME_KEY)) {
				token = cookie.getValue();
				break;
			}
		}
		if(token != null) {
			httpServletRequest.setAttribute(AccessConst.DEFAULT_TOKEN_NAME_KEY, token);
			httpServletRequest.getSession().setAttribute(AccessConst.DEFAULT_TOKEN_NAME_KEY, token);
		}
		return token == null ? null : token.toString();
	}
	
	//token模式
	private static String getTokenIdByModeToken() {
		HttpServletRequest httpServletRequest = requestLocal.get();
		if(httpServletRequest == null) {
		   return null;
		}
		String tokenName = modeProperties.getTokenName();
		String agent = ServletHolder.getMultipleAgent(requestLocal.get(), modeProperties.getTokenAgent());
		return ServletHolder.getMultipleTokenId(httpServletRequest, tokenName, agent);
	}
	
	//#持久化用户信息
	public static void renewalCache(String tokenId,String agent,AccessPrincipal user) {
		final int maxAge = accessSessionManager.getMaxInactiveInterval();
		final Long expire = System.currentTimeMillis() + ( maxAge * 1000);
		final String userId = user.getUserId();
		switch (modeProperties.getMode()) {
			case SESSION:
				cacheService.set(String.format(SESSION_MODE_CACHE_KEY, tokenId), tokenId, maxAge); //token持久化
				cacheService.set(String.format(SESSION_MODE_CACHE_USER_INFO, tokenId), user, maxAge); //token用户信息持久化
				cacheService.set(String.format(SESSION_MODE_CACHE_EXPIRES_KEY, tokenId), expire, maxAge);
				if(!StringUtils.isEmpty(userId)) {
				   cacheService.set(String.format(SESSION_MODE_CACHE_USER_ID, userId), user, maxAge);
				}
				break;
			case TOKEN:
				cacheService.set(String.format(TOKEN_MODE_CACHE_KEY, agent,tokenId), tokenId, maxAge); //token持久化
				cacheService.set(String.format(TOKEN_MODE_CACHE_USER_INFO,agent,tokenId), user, maxAge); //token用户信息持久化
				cacheService.set(String.format(TOKEN_MODE_CACHE_EXPIRES_KEY, agent,tokenId), expire, maxAge);
				if(!StringUtils.isEmpty(userId)) {
				   cacheService.set(String.format(TOKEN_MODE_CACHE_USER_ID, agent,userId), user, maxAge);
				}
				break;
		}
		logger.info("renewalCache tokenId = {} userId = {} , userName = {} , renewal success expireTime = {} ",tokenId,userId,user.getName(),DateKti.format(expire));
	}
	
	public static void builder(AccessSessionManager accessManager,SessionCookieConfig config) {
		if(accessSessionManager == null) {
			synchronized(AccessContext.class) {
				if(accessSessionManager == null) {
				   accessSessionManager = accessManager;
				}
			}
		}
		if(modeProperties == null) {
			synchronized(AccessContext.class) {
				if(modeProperties == null) {
				   modeProperties = accessManager.getModeProperties();
				}
			}
		}
		if(cacheService == null) {
			synchronized(AccessContext.class) {
				if(cacheService == null) {
				   cacheService = accessSessionManager.getCache();
				}
			}
		}
		if(cookieConfig == null) {
			synchronized(AccessContext.class) {
				if(cookieConfig == null) {
				   cookieConfig = config;
				}
			}
		}
	}
	
}

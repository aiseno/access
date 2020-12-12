package com.github.aiseno.access.listener.session.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.github.aiseno.access.cache.AccessOnlineCache;
import com.github.aiseno.access.cache.helper.ICache;
import com.github.aiseno.access.holder.AccessContext;
import com.github.aiseno.access.holder.AccessContextHolder;
import com.github.aiseno.access.holder.ServletHolder;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.listener.handle.AccessHandle;
import com.github.aiseno.access.listener.handle.AccessHandleApplicationListener;
import com.github.aiseno.access.listener.handle.events.AccessHandleEvent.AccessHttpServletObject;
import com.github.aiseno.access.properties.AccessModeProperties;
import com.github.aiseno.access.properties.AccessPrincipal;
import com.github.aiseno.access.util.DateKti;

/**
 * @author admin
 */
@SuppressWarnings("rawtypes")
public class AccessLogoutImpl implements AccessDefault {

	private static Logger logger = LoggerFactory.getLogger(AccessHandleApplicationListener.class);
	
	private final ICache cacheService;
	
	private final HttpServletRequest request;
	
	private final HttpServletResponse response;
	
	private final AccessHandle accessLogoutHandle;
	
	private final AccessModeProperties accessModeProperties;
	
	private AccessLogoutImpl(AccessHttpServletObject eventObject) {
		final AccessSessionManager accessManager = AccessContext.getAccessManager();
		this.request = eventObject.getRequest();
		this.response = eventObject.getResponse();
		this.cacheService = AccessContext.getCacheService();
		this.accessLogoutHandle = accessManager.getAccessHandle();
		this.accessModeProperties = accessManager.getModeProperties();
	}
	
	public static AccessLogoutImpl builder(AccessHttpServletObject event) {
		return new AccessLogoutImpl(event);
	}
	
	@SuppressWarnings("unchecked")
	private ResponseEntity handleResponse() {
		ResponseEntity responseEntity = null;
		try {
			responseEntity = accessLogoutHandle.logout(ServletHolder.getParameter(request), request, response);
		} catch (Exception e) {
			logger.warn("logout handleResponse ex = {}",e.getMessage());
		}
		return responseEntity;
	}
	
	@Override
	public ResponseEntity doHandle() {
		final AccessPrincipal accessPrincipal = AccessContextHolder.getAccessPrincipal();
		final ResponseEntity responseEntity = this.handleResponse();
		try {
			switch (accessModeProperties.getMode()) {
				case SESSION:
					this.doSessionModeHandle(accessPrincipal);
					break;
				default:
					this.doTokenModeHandle(accessPrincipal);
					break;
			}
			AccessContext.clears();
			request.getSession().invalidate();
		} catch (Exception e) {
			logger.warn(e.getMessage());
		} 
		return responseEntity;
	}
	
	//Session 模式
	@Override
	public void doSessionModeHandle(AccessPrincipal accessPrincipal) {
		final SessionCookieConfig config = AccessContext.getCookieConfig();
		final CookieHolder cookieHolder = CookieHolder.builder(config, response);
		final HttpSession session = request.getSession();
		String tokenId = cookieHolder.getToken(request);
		if(tokenId == null) {
		   tokenId = ServletHolder.getDefaultTokenId(request, TOKEN_KEY);
		}
		if(tokenId == null) {
			return;
		}
		if(!tokenId.equals(accessPrincipal.getTokenId())) {
			return;
		}
		cookieHolder.removeCookie(TOKEN_KEY);
		session.removeAttribute(TOKEN_KEY);
		request.removeAttribute(TOKEN_KEY);
		cacheService.remove(String.format(SESSION_MODE_CACHE_KEY, tokenId)); //token持久化
		cacheService.remove(String.format(SESSION_MODE_CACHE_USER_INFO, tokenId)); //token用户信息持久化
		cacheService.remove(String.format(SESSION_MODE_CACHE_EXPIRES_KEY, tokenId));
		String userId = accessPrincipal.getUserId();
		if(!StringUtils.isEmpty(userId)) {
		   session.removeAttribute(USER_KEY);
		   request.removeAttribute(USER_KEY);
		   cacheService.remove(String.format(SESSION_MODE_CACHE_USER_ID, userId));
		}
		AccessOnlineCache.remove(tokenId);
		logger.info("logout success , tokenId = {} , createTime = {} user = {} ",tokenId,DateKti.format(new Date()),accessPrincipal.getName());
	}

	//Token 模式
	@Override
	public void doTokenModeHandle(AccessPrincipal accessPrincipal) {
		final String agent = ServletHolder.getMultipleAgent(request, accessModeProperties.getTokenAgent());
		if(StringUtils.isEmpty(agent)) {
			logger.error("tokenAgent => {} nust be not null. logout failure.登出失败.",accessModeProperties.getTokenAgent()); 
			return;
		}
		final boolean isMuls = accessModeProperties.isMultipleLogin();
		final String userId = accessPrincipal.getUserId();
		final HttpSession session = request.getSession();
		if(isMuls) {
			List<String> perfixs = Arrays.asList(accessModeProperties.getTokenPrefix());
			if(!perfixs.contains(agent)) {
				logger.error("tokenPrefix not contain {} , tokenAgent invalid . logout failure.登出失败.",agent); 
				return;
			}
		}
		String tokenId = ServletHolder.getMultipleTokenId(request, accessModeProperties.getTokenName(), agent);
		if(tokenId == null) {
		   return;
		}
		if(!tokenId.equals(accessPrincipal.getTokenId())) {
		   return;
		}
		session.removeAttribute(agent + TOKEN_KEY);
		request.removeAttribute(agent + TOKEN_KEY);
		cacheService.remove(String.format(TOKEN_MODE_CACHE_KEY, agent,tokenId)); //token持久化
		cacheService.remove(String.format(TOKEN_MODE_CACHE_USER_INFO,agent,tokenId)); //token用户信息持久化
		cacheService.remove(String.format(TOKEN_MODE_CACHE_EXPIRES_KEY, agent,tokenId));
		if(!StringUtils.isEmpty(userId)) {
			session.removeAttribute(agent + USER_KEY);
			request.removeAttribute(agent + USER_KEY);
			cacheService.remove(String.format(TOKEN_MODE_CACHE_USER_ID, agent,userId));
		}
		AccessOnlineCache.remove(tokenId);
		logger.info("logout success , tokenId = {} , createTime = {} user = {} ",tokenId,DateKti.format(new Date()),accessPrincipal.getName());
	}
	
}

package com.github.aiseno.access.listener.session.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.github.aiseno.access.cache.AccessOnlineCache;
import com.github.aiseno.access.holder.AccessContext;
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
public class AccessLoginImpl implements AccessDefault {

	private static Logger logger = LoggerFactory.getLogger(AccessHandleApplicationListener.class);
	
	private final HttpServletRequest request;
	
	private final HttpServletResponse response;
	
	private final AccessModeProperties accessModeProperties;
	
	private final AccessHandle accessLoginHandle;
	
	private final Integer maxAge;
	
	private AccessLoginImpl(AccessHttpServletObject event) {
		final AccessSessionManager accessManager = AccessContext.getAccessManager();
		this.request = event.getRequest();
		this.response = event.getResponse();
		this.maxAge = accessManager.getMaxInactiveInterval();
		this.accessLoginHandle = accessManager.getAccessHandle();
		this.accessModeProperties = accessManager.getModeProperties();
	}
	
	public static AccessLoginImpl builder(AccessHttpServletObject event) {
		return new AccessLoginImpl(event);
	}
	
	@SuppressWarnings("unchecked")
	private ResponseEntity handleResponse() {
		ResponseEntity responseEntity = null;
		try {
			responseEntity = accessLoginHandle.login(ServletHolder.getParameter(request), request, response);
		} catch (Exception e) {
			logger.warn("logout handleResponse ex = {}",e.getMessage());
		}
		return responseEntity;
	}
	
	//获取用户信息
	private AccessPrincipal getAccessPrincipal() {
		return accessLoginHandle.setLoginSuccess();
	}
	
	//登录成功操作
	@Override
	public ResponseEntity doHandle() {
		final ResponseEntity result = this.handleResponse();
		final AccessPrincipal accessPrincipal = this.getAccessPrincipal();
		if(accessPrincipal == null) {
		   return result;
		}
		if(StringUtils.isEmpty(accessPrincipal.getTokenId())) {
		   accessPrincipal.setTokenId(generatorTokenId());
		}
		switch (accessModeProperties.getMode()) {
			case SESSION:
				this.doSessionModeHandle(accessPrincipal);
				break;
			default:
				this.doTokenModeHandle(accessPrincipal);
				break;
		}
		return result;
	}

	//Session 模式
	@Override
	public void doSessionModeHandle(AccessPrincipal accessPrincipal) {
		final String tokenId = accessPrincipal.getTokenId();
		final SessionCookieConfig config = request.getServletContext().getSessionCookieConfig();
		final Long expire = System.currentTimeMillis() + (maxAge * 1000);
		boolean  bool = CookieHolder.builder(config,response).addCookie(TOKEN_KEY, tokenId, maxAge);
		if(bool) {
			final HttpSession session = request.getSession();
			AccessContext.renewalCache(tokenId,null,accessPrincipal);
			session.setAttribute(TOKEN_KEY, tokenId);
			request.setAttribute(TOKEN_KEY, tokenId);
			String userId = accessPrincipal.getUserId();
			if(!StringUtils.isEmpty(userId)) {
				session.setAttribute(USER_KEY, userId);
				request.setAttribute(USER_KEY, userId);
			}
		}
		AccessContext.setRequestLocal(request);
		String createTime = DateKti.format(new Date()) , expireTime = DateKti.format(expire);
		AccessOnlineCache.set(tokenId, accessPrincipal);
		logger.info("login success , username = {} , tokenId = {} , createTime = {} , expireTime = {} ",accessPrincipal.getName(),tokenId,createTime,expireTime);
	}
	
	//Token 模式
	@Override
	public void doTokenModeHandle(AccessPrincipal accessPrincipal) {
		final String agent = ServletHolder.getMultipleAgent(request, accessModeProperties.getTokenAgent());
		if(StringUtils.isEmpty(agent)) {
			logger.error("tokenAgent => {} nust be not null. login failure.登录失败.",accessModeProperties.getTokenAgent()); 
			return;
		}
		final boolean isMuls = accessModeProperties.isMultipleLogin();
		final String tokenId = accessPrincipal.getTokenId();
		final String userId = accessPrincipal.getUserId();
		final HttpSession session = request.getSession();
		if(isMuls) {
			List<String> perfixs = Arrays.asList(accessModeProperties.getTokenPrefix());
			if(!perfixs.contains(agent)) {
				logger.error("tokenPrefix not contain {} , tokenAgent invalid . login failure.登录失败.",agent); 
				return;
			}
		}
		AccessContext.renewalCache(tokenId,agent,accessPrincipal);
		session.setAttribute(agent + TOKEN_KEY, tokenId);
		request.setAttribute(agent + TOKEN_KEY, tokenId);
		if(!StringUtils.isEmpty(userId)) {
			session.setAttribute(agent + USER_KEY, userId);
			request.setAttribute(agent + USER_KEY, userId);
		}
		AccessContext.setRequestLocal(request);
		String createTime = DateKti.format(new Date()) , expireTime = DateKti.format(System.currentTimeMillis() + (maxAge * 1000));
		AccessOnlineCache.set(tokenId, accessPrincipal);
		logger.info("login success , username = {} , tokenId = {} , createTime = {} , expireTime = {} ",accessPrincipal.getName(),tokenId,createTime,expireTime);
	}

	// generator token sign.
	private static String generatorTokenId() {
  		return UUID.randomUUID().toString().replace("-", "");
  	}
}

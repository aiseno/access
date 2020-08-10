package com.aiseno.access.listener.session;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.aiseno.access.cache.helper.ICache;
import com.aiseno.access.consts.AccessConst;
import com.aiseno.access.consts.AccessConst.AccessServletOptEnums;
import com.aiseno.access.holder.AccessContextHolder;
import com.aiseno.access.holder.ServletHolder;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.listener.handle.AccessHandle;
import com.aiseno.access.listener.handle.AccessHandleEvent;
import com.aiseno.access.listener.handle.AccessHandleEvent.AccessHttpServletObject;
import com.aiseno.access.properties.AccessPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
public class AccessHandleListener implements ApplicationListener<AccessHandleEvent> {

	private static Logger logger = LoggerFactory.getLogger(AccessHandleListener.class);
		
	@Override
	public void onApplicationEvent(AccessHandleEvent event) {
		if(event == null || event.getSccessHttpServletObject() == null) {
			logger.error("AccessEvent must not be null.");
			return;
		}
		this.handle(event.getSccessHttpServletObject());
	}
	
	/**
	 * -处理入口
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	protected void handle(AccessHttpServletObject object) {
		AccessSessionManager accessSessionManager = object.getAccessSessionManager();
		AccessHandle accessHandle = accessSessionManager.getAccessHandle();
		AccessServletOptEnums opt = object.getOpt();
		HttpServletRequest request = object.getRequest();
		HttpServletResponse response = object.getResponse();
		HttpSession session = request.getSession();
		ResponseEntity responseEntity = null;
		Map<String, Object> params = ServletHolder.getParameter(request);
		switch (opt) {
			case LOGIN:
				logger.info("call accessHandle login ...");
				responseEntity = accessHandle.login(params,request, response);
				this.loginSuccess(accessSessionManager,session,accessHandle.getAccessPrincipal());
				break;
			case LOGOUT:
				logger.info("call accessHandle logout ...");
				responseEntity = accessHandle.logout(params,request, response);
				this.logoutSuccess(accessSessionManager,session);
				break;
			default:
				responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("not support..");
				logger.error("AbstractAccessListener handle type error. {}" , opt);
				break;
		}
		this.transformResponseEntity(responseEntity, response);
	}
	
	/**
	 * -登录成功
	 * @param session
	 * @param accessPrincipal
	 */
	protected void loginSuccess(AccessSessionManager accessSessionManager,HttpSession session,AccessPrincipal accessPrincipal) {
		if(accessPrincipal == null) {
			return;
		}
		String tokenId = accessPrincipal.getSessionId();
		if(tokenId == null) {
		   tokenId = ServletHolder.generatorTokenId();
		   accessPrincipal.setSessionId(tokenId);
		}
		long creationTime = session.getCreationTime();
		ICache cacheService = accessSessionManager.getCache();
		Integer expireTime = accessSessionManager.getMaxInactiveInterval();
		cacheService.set(AccessConst.CACHE_TOKEN_KEY + tokenId, accessPrincipal, expireTime);
		//存入session
		session.setAttribute(AccessConst.CACHE_TOKEN_KEY, tokenId);
		session.setAttribute(AccessConst.CACHE_TOKEN_EXPIRES_KEY, (creationTime +  expireTime * 1000));
		AccessContextHolder.setServletCacheLocal(cacheService, session);
	}
	
	/**
	 * -退出成功
	 * @param session
	 */
	protected void logoutSuccess(AccessSessionManager accessSessionManager,HttpSession session) {
		 session.invalidate();
	}
	
	/**
	 * -转换返回
	 * 
	 * @param responseEntity
	 * @param response
	 */
 	protected void transformResponseEntity(ResponseEntity responseEntity, HttpServletResponse response) {
		if (responseEntity == null) {
			responseEntity = ResponseEntity.ok().build();
		}
		HttpHeaders httpHeaders = responseEntity.getHeaders();
		if (httpHeaders != null && !httpHeaders.isEmpty()) {
			httpHeaders.forEach((k, v) -> {
				response.setHeader(k, v.get(0));
			});
		}
		response.setStatus(responseEntity.getStatusCodeValue());
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			Object body = responseEntity.getBody();
			out.write(new ObjectMapper().writeValueAsBytes(body == null ? "" : body));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}

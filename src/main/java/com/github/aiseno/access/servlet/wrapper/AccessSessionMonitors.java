package com.github.aiseno.access.servlet.wrapper;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.github.aiseno.access.cache.AccessOnlineCache;
import com.github.aiseno.access.cache.helper.ICache;
import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.holder.AccessContext;
import com.github.aiseno.access.holder.ServletHolder;
import com.github.aiseno.access.listener.AccessSessionManager;
import com.github.aiseno.access.listener.session.impl.AccessDefault;
import com.github.aiseno.access.properties.AccessModeProperties;
import com.github.aiseno.access.properties.AccessPrincipal;

public abstract class AccessSessionMonitors implements AccessDefault {
	
	public static void createMonitor(HttpServletRequest request,HttpServletResponse response , AccessPrincipal accessPrincipal) {
		if(accessPrincipal == null) {
			return;
		}
		AccessSessionManager accessSessionManager = AccessContext.getAccessManager();
		AccessModeProperties accessProperties = accessSessionManager.getModeProperties();
		Integer maxInterval = accessSessionManager.getMaxInactiveInterval();
		ICache cacheService = AccessContext.getCacheService();
		switch (accessProperties.getMode()) {
			case SESSION:
				builderSessionMonitor(request,response,accessPrincipal,cacheService,maxInterval);
				break;
			default:
				builderTokenMonitor(request,accessPrincipal,cacheService,accessProperties,maxInterval);
				break;
		}
	}
	
	//Session 续期
	private static void builderSessionMonitor(HttpServletRequest request,HttpServletResponse response,AccessPrincipal accessPrincipal,ICache cacheService,Integer maxInterval) {
		final String tokenId = ServletHolder.getDefaultTokenId(request,TOKEN_KEY);
		if(StringUtils.isEmpty(tokenId)) {
			return;
		}
	    Long expire = cacheService.get(String.format(SESSION_MODE_CACHE_EXPIRES_KEY, tokenId));
	    if(expire != null && expire > 0) {
		   long difference     = (expire - System.currentTimeMillis()) / 1000;
		   //过期时间小于四分一
		   if(difference < (maxInterval / 4)) {
			   String tokenCookieKey = AccessConst.DEFAULT_TOKEN_NAME_KEY;
			   synchronized (AccessSessionMonitors.class) {
				   SessionCookieConfig config = AccessContext.getCookieConfig();
				   boolean bool = AccessDefault.CookieHolder.builder(config,response).addCookie(tokenCookieKey, tokenId,maxInterval);
				   if(bool) {
					  AccessContext.renewalCache(tokenId,null,accessPrincipal);
					  AccessOnlineCache.set(tokenId, accessPrincipal);
				   }
			   }
		   }
	   }
	}
	
	//token模式
	private static void builderTokenMonitor(HttpServletRequest request,AccessPrincipal accessPrincipal,ICache cacheService,AccessModeProperties accessProperties,Integer maxInterval) {
		final String agent = ServletHolder.getMultipleAgent(request, accessProperties.getTokenAgent());
		if(StringUtils.isEmpty(agent)) {
			return;
		}
		final String tokenId = ServletHolder.getMultipleTokenId(request, accessProperties.getTokenName(), agent);
		if(StringUtils.isEmpty(tokenId)) {
			return;
		}
	    Long expire = cacheService.get(String.format(TOKEN_MODE_CACHE_EXPIRES_KEY, agent,tokenId));
	    if(expire != null && expire > 0) {
		   long difference     = (expire - System.currentTimeMillis()) / 1000;
		   //过期时间小于四分一
		   if(difference < (maxInterval / 4)) {
			   synchronized (AccessSessionMonitors.class) {
				  AccessContext.renewalCache(tokenId,agent,accessPrincipal);
				  AccessOnlineCache.set(tokenId, accessPrincipal);
			   }
		   }
	   }
	}
	
}

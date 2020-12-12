package com.github.aiseno.access.listener.session.impl;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.properties.AccessPrincipal;

/**
 * @author admin
 */
@SuppressWarnings("rawtypes")
public interface AccessDefault extends AccessConst {

	//default
	public static final String TOKEN_KEY = DEFAULT_TOKEN_NAME_KEY;
	public static final String USER_KEY = DEFAULT_USER_NAME_KEY;
	
	//Session 模式
	public static final String SESSION_MODE_CACHE_KEY         = CACHE_TOKEN_KEY + "%s";
	public static final String SESSION_MODE_CACHE_USER_ID     = TOKEN_KEY + "%s";
	public static final String SESSION_MODE_CACHE_USER_INFO   = CACHE_USER_KEY + "%s";
	public static final String SESSION_MODE_CACHE_EXPIRES_KEY = CACHE_TOKEN_EXPIRES_KEY + "%s";
	
	//Token模式
	public static final String TOKEN_MODE_CACHE_KEY         = "%s" + CACHE_TOKEN_KEY + "%s";
	public static final String TOKEN_MODE_CACHE_USER_ID     = "%s" + TOKEN_KEY + "%s";
	public static final String TOKEN_MODE_CACHE_USER_INFO   = "%s" + CACHE_USER_KEY + "%s";
	public static final String TOKEN_MODE_CACHE_EXPIRES_KEY = "%s" + CACHE_TOKEN_EXPIRES_KEY + "%s";
	
	ResponseEntity doHandle();
	
	void doSessionModeHandle(AccessPrincipal accessPrincipal);
	
	void doTokenModeHandle(AccessPrincipal accessPrincipal);
	
	static class CookieHolder {

		private final SessionCookieConfig config;
		
		private final HttpServletResponse response;
		
		private CookieHolder(SessionCookieConfig config,HttpServletResponse response) {
			this.config = config;
			this.response = response;
		}

		public static CookieHolder builder(SessionCookieConfig config,HttpServletResponse response) {
			return new CookieHolder(config,response);
		}
		
		public boolean addCookie(String name, String value, int maxAge) {
			try {
				final Cookie cookie = this.getCookie(name,value);
				cookie.setValue(value);
				if (maxAge > 0) {
					cookie.setMaxAge(maxAge);
				}
				response.addCookie(cookie);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		public void removeCookie(String name) {
			Cookie cookie = this.getCookie(name,null);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		
		public String getToken(HttpServletRequest request) {
			Cookie cookies[] = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(TOKEN_KEY)) {
					return cookie.getValue();
				}
			}
			return null;
		}
		
		//获取cookie
		private Cookie getCookie(String name,String value) {
			final Cookie cookie = new Cookie(name,value);
			cookie.setComment(config.getComment());
			cookie.setPath(config.getPath());
			if(!StringUtils.isEmpty(config.getDomain())) {
			   cookie.setDomain(config.getDomain());
			}
			return cookie;
		}
	}

}

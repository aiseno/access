package com.github.aiseno.access.consts;

import java.util.Arrays;
import java.util.List;

public interface AccessConst {
	
	public static final String DEFAULT_LOGIN_URI = "/access/login/v1";
	
	public static final String DEFAULT_LOGOUT_URI = "/access/logout/v1";
	
	public static final String DEFAULT_SUCCESS_URI = "/";
	
	public static final List<String> DEFAULT_VALID_URI = Arrays.asList("/*","/**");
	
	//默认token存储键
	public static final String DEFAULT_TOKEN_NAME_KEY = "_ACCESS_TOKEN_ID_";
	public static final String DEFAULT_USER_NAME_KEY  = "_ACCESS_USER_ID_";
	public static final String DEFAULT_AGENT_KEY      = "_ACCESS_AGENT_ID_";
	
	//缓存相关Key
	public static final String CACHE_TOKEN_KEY = "access:token:key:";
	public static final String CACHE_USER_KEY  = "access:token:user:";
	public static final String CACHE_TOKEN_EXPIRES_KEY = "access:token:expires:key:";
	
	
	public static final Integer CACHE_SESSION_EXPIRE_TIME = 1800;
	
	
	public static enum AccessServletOptEnums {
		LOGIN ,
		LOGOUT
	}

}

package com.aiseno.access.consts;

import java.util.Arrays;
import java.util.List;

public interface AccessConst {
	
	public static final String DEFAULT_LOGIN_URI = "/access/login/v1";
	
	public static final String DEFAULT_LOGOUT_URI = "/access/logout/v1";
	
	public static final String DEFAULT_SUCCESS_URI = "/";
	
	public static final List<String> DEFAULT_VALID_URI = Arrays.asList("/*","/**");
	
	public static final String CACHE_TOKEN_KEY = "session:token:key:";
	
	public static final String CACHE_TOKEN_EXPIRES_KEY = "session:expires:key";
	
	public static final Integer CACHE_SESSION_EXPIRE_TIME = 1800;
	
	public static enum AccessServletOptEnums {
		LOGIN ,
		LOGOUT
	}

}

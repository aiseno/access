package com.github.aiseno.access.listener.handle;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.github.aiseno.access.properties.AccessPrincipal;

public interface AccessHandle<T> {
	
	ResponseEntity<T> login(Map<String, Object> params,HttpServletRequest request , HttpServletResponse response);
	
	ResponseEntity<T> logout(Map<String, Object> params,HttpServletRequest request , HttpServletResponse response);
	
	AccessPrincipal setLoginSuccess();
	
	default AccessPrincipal getAccessPrincipal() {
		return setLoginSuccess();
	}
}

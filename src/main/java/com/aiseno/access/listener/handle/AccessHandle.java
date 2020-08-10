package com.aiseno.access.listener.handle;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.aiseno.access.properties.AccessPrincipal;

public interface AccessHandle<T> {
	
	/**
	 * - 登录接口
	 * @param request
	 * @param response
	 */
	ResponseEntity<T> login(Map<String, Object> params,HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * -登出接口
	 * @param request
	 * @param response
	 */
	ResponseEntity<T> logout(Map<String, Object> params,HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * -设置登录成功数据
	 * @return
	 */
	AccessPrincipal setLoginSuccess();
	
	/**
	 * -获取登录信息
	 * @return
	 */
	default AccessPrincipal getAccessPrincipal() {
		return setLoginSuccess();
	}
}

package com.aiseno.test.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.aiseno.access.listener.handle.AccessHandle;
import com.aiseno.access.properties.AccessPrincipal;

public class AccessHandleImpl implements AccessHandle <Object> {

	private AccessPrincipal principal = null;
	
	@Override
	public ResponseEntity<Object> login(Map<String, Object> params,HttpServletRequest request, HttpServletResponse response) {
		System.out.println("--------login----------");
		principal = new AccessPrincipal();
		principal.setId("666");
		principal.setName(params.get("username").toString());
		principal.setAdmin(false);
		return ResponseEntity.ok("登录成功.");
	}

	@Override
	public ResponseEntity<Object> logout(Map<String, Object> params,HttpServletRequest request, HttpServletResponse response) {
		return ResponseEntity.ok("退出成功");
	}

	@Override
	public AccessPrincipal setLoginSuccess() {
		return principal;
	}
}

package com.github.aiseno.test.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.github.aiseno.access.listener.handle.AccessCustomHandle;

public class AccessCustomHandleImpl implements AccessCustomHandle {

	

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest) throws IOException, ServletException {
		return false;
	}
	
	

}

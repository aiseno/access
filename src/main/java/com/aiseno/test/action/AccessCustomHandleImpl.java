package com.aiseno.test.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.aiseno.access.listener.handle.AccessCustomHandle;

public class AccessCustomHandleImpl implements AccessCustomHandle {

	@Override
	public void doCustomFilter(Map<String, Object> params, ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}
	
	

}

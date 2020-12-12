package com.github.aiseno.access.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.consts.AccessConst.AccessServletOptEnums;
import com.github.aiseno.access.exception.AccessFilterException;
import com.github.aiseno.access.listener.handle.events.AccessHandleEvent.AccessHttpServletObject;

public class AccessHandleServlet extends HttpServlet {
	
	private static final long serialVersionUID = -842952252873510888L;

	private final ApplicationContext applicationContext; 
	
	public AccessHandleServlet (ApplicationContext applicationContext) {
		 this.applicationContext = applicationContext;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String path = request.getServletPath();
		if(!AccessConst.DEFAULT_LOGOUT_URI.equals(path)) {
			throw new AccessFilterException("Invalid logout request!");
		};
		this.publish(request, response,AccessServletOptEnums.LOGOUT);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String path = request.getServletPath();
		if(!AccessConst.DEFAULT_LOGIN_URI.equals(path)) {
			throw new AccessFilterException("Invalid login request!");
		};
		this.publish(request, response,AccessServletOptEnums.LOGIN);
	}
	
	private void publish(HttpServletRequest request, HttpServletResponse response,AccessServletOptEnums opt) {
		applicationContext.publishEvent(AccessHttpServletObject.create(request, response,opt).builder());
	}
	
}

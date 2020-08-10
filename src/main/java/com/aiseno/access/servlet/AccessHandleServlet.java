package com.aiseno.access.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiseno.access.consts.AccessConst.AccessServletOptEnums;
import com.aiseno.access.listener.AccessSessionManager;
import com.aiseno.access.listener.handle.AccessHandleEvent.AccessHttpServletObject;

public class AccessHandleServlet extends HttpServlet {
	
	private static final long serialVersionUID = -842952252873510888L;

	private AccessSessionManager accessSessionManager; 
	
	public AccessHandleServlet (AccessSessionManager manager) {
		 this.accessSessionManager = manager;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.publish(request, response,AccessServletOptEnums.LOGOUT);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.publish(request, response,AccessServletOptEnums.LOGIN);
	}
	
	private void publish(HttpServletRequest request, HttpServletResponse response,AccessServletOptEnums opt) {
		accessSessionManager.getApplicationContext().publishEvent(AccessHttpServletObject.create(request, response,opt,accessSessionManager).builder());
	}
	
}

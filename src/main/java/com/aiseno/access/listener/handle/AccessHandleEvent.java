package com.aiseno.access.listener.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import com.aiseno.access.consts.AccessConst.AccessServletOptEnums;
import com.aiseno.access.listener.AccessSessionManager;

public class AccessHandleEvent extends ApplicationEvent {

	private static final long serialVersionUID = 419213641648238184L;
	
	private AccessHttpServletObject sccessHttpServletObject;
	
	public AccessHandleEvent(AccessHttpServletObject source) {
		super(source);
		this.sccessHttpServletObject = (AccessHttpServletObject) source;
	}
	
	public AccessHttpServletObject getSccessHttpServletObject() {
		return sccessHttpServletObject;
	}
	
	public static class AccessHttpServletObject {
		
		private HttpServletRequest request;
		
		private HttpServletResponse response;
		
		private AccessServletOptEnums opt;

		private AccessSessionManager accessSessionManager;
		
		private AccessHttpServletObject(HttpServletRequest request, HttpServletResponse response , AccessServletOptEnums opt , AccessSessionManager accessManager) {
			super();
			this.request = request;
			this.response = response;
			this.opt = opt;
			this.accessSessionManager = accessManager;
		}
		
		public static AccessHttpServletObject create(HttpServletRequest request, HttpServletResponse response , AccessServletOptEnums opt , AccessSessionManager accessSessionManager) {
			return new AccessHttpServletObject(request,response , opt , accessSessionManager);
		}
		public AccessHandleEvent builder() {
			return new AccessHandleEvent(this);
		}
		public HttpServletRequest getRequest() {
			return request;
		}
		public void setRequest(HttpServletRequest request) {
			this.request = request;
		}
		public HttpServletResponse getResponse() {
			return response;
		}
		public void setResponse(HttpServletResponse response) {
			this.response = response;
		}
		public AccessServletOptEnums getOpt() {
			return opt;
		}
		public void setOpt(AccessServletOptEnums opt) {
			this.opt = opt;
		}
		public AccessSessionManager getAccessSessionManager() {
			return accessSessionManager;
		}
		public void setAccessSessionManager(AccessSessionManager accessSessionManager) {
			this.accessSessionManager = accessSessionManager;
		}
	}
	
}

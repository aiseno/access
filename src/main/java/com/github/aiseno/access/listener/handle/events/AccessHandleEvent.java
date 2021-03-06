package com.github.aiseno.access.listener.handle.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import com.github.aiseno.access.consts.AccessConst.AccessServletOptEnums;

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
		
		private AccessHttpServletObject(HttpServletRequest request, HttpServletResponse response , AccessServletOptEnums opt) {
			super();
			this.request = request;
			this.response = response;
			this.opt = opt;
		}
		
		public static AccessHttpServletObject create(HttpServletRequest request, HttpServletResponse response , AccessServletOptEnums opt) {
			return new AccessHttpServletObject(request,response , opt);
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
	}
	
}

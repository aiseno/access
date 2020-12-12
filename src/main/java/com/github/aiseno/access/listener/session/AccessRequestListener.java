package com.github.aiseno.access.listener.session;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import com.github.aiseno.access.holder.AccessContext;

/**
 * @author admin
 */
public class AccessRequestListener implements ServletRequestListener {
	
	@Override
	public void requestInitialized(ServletRequestEvent requestEvent) {
		if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
			throw new IllegalArgumentException("Request is not a HttpServletRequest: " + requestEvent.getServletRequest());
		}
		AccessContext.setRequestLocal((HttpServletRequest) requestEvent.getServletRequest());
	}
	
	@Override
	public void requestDestroyed(ServletRequestEvent requestEvent) {
		AccessContext.clears();
	}

}

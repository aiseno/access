package com.aiseno.access.servlet.filter.handle;

import com.aiseno.access.exception.IFilterException;
import com.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;

public interface AccessHandleFilter {
	
	boolean handle(String requestURI , AccessPathMatcherSupport support);

	IFilterException exception();
	
}

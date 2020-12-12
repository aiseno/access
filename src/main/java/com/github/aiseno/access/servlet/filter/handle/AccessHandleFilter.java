package com.github.aiseno.access.servlet.filter.handle;

import com.github.aiseno.access.exception.IFilterException;
import com.github.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;

public interface AccessHandleFilter {
	
	boolean handle(String requestURI , AccessPathMatcherSupport support);

	IFilterException exception();
	
}

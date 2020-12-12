package com.github.aiseno.access.servlet.filter.handle;

import com.github.aiseno.access.exception.IFilterException;
import com.github.aiseno.access.exception.UnauthorizedAccessException;
import com.github.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;

public class AccessHandleFilterImpl extends AbstractAccessHandleFilter implements AccessHandleFilter {

	private static final UnauthorizedAccessException NOT_LONIX_EX = new UnauthorizedAccessException("Unauthorized. Please login first");

	private static ThreadLocal<IFilterException> exception = new ThreadLocal<>();
	
	@Override
	public boolean handle(String uri, AccessPathMatcherSupport support) {
		if (support.isValidIgnoreUri(uri))
			return true;
		if (support.isSuccessUri(uri))
			return doit(); 
		if (support.isValidLogoutUri(uri))
			return doit();
		if (support.isValidUri(uri)) 
			return doit();
		return true;
	}

	@Override
	public IFilterException exception() {
		return exception.get();
	}

	@Override
	boolean doit() {
		if (!exist()) {
			exception.set(NOT_LONIX_EX);
			return false;
		}
		return true;
	}
}

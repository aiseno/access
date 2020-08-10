package com.aiseno.access.servlet.filter.handle;

import com.aiseno.access.exception.IFilterException;
import com.aiseno.access.holder.AccessContextHolder;

public abstract class AbstractAccessHandleFilter {
	
	static boolean isAlreadyLogin() {
		return AccessContextHolder.isAlreadyLogin();
	}
	
	abstract boolean doit(IFilterException exception);
	
}

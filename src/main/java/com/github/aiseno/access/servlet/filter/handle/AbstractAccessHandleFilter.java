package com.github.aiseno.access.servlet.filter.handle;

import com.github.aiseno.access.holder.AccessContextHolder;

public abstract class AbstractAccessHandleFilter {
	
	static boolean exist() {
		return AccessContextHolder.isAlreadyLogin();
	}
	
	abstract boolean doit();
	
}

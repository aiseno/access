package com.github.aiseno.access.listener.handle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface AccessCustomHandle {

	boolean preHandle(HttpServletRequest httpServletRequest)  throws IOException, ServletException;
	
}

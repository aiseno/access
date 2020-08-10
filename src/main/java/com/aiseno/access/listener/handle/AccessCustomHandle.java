package com.aiseno.access.listener.handle;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface AccessCustomHandle {

	void doCustomFilter(Map<String,Object> params,  ServletRequest request, ServletResponse response, FilterChain chain)  throws IOException, ServletException;
}

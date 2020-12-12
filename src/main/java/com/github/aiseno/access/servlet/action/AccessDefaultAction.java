package com.github.aiseno.access.servlet.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.aiseno.access.consts.AuthorizedConst;
import com.github.aiseno.access.exception.AccessFilterException;

@RestController
public class AccessDefaultAction implements AuthorizedConst {

	@RequestMapping(EXCEPTION_MOTHED_NAME)
	public Object authorized401(HttpServletRequest request) {
		AccessFilterException exception = (AccessFilterException) request.getAttribute(EXCEPTION_ATTR_NAME);
		throw exception;
	}
}

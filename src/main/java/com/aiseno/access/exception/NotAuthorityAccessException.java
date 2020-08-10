package com.aiseno.access.exception;

public class NotAuthorityAccessException extends AccessFilterException {

	private static final long serialVersionUID = 1256237228958556804L;

	public NotAuthorityAccessException (String msg) {
		super(msg);
	}
	
}

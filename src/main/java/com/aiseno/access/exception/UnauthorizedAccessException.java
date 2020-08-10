package com.aiseno.access.exception;

public class UnauthorizedAccessException extends AccessFilterException {

	private static final long serialVersionUID = 1256237228958556804L;

	public UnauthorizedAccessException (String msg) {
		super(msg);
	}
	
}

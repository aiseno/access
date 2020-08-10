package com.aiseno.access.exception;

public class AccessFilterException extends RuntimeException implements IFilterException {

	private static final long serialVersionUID = 8049275307051574204L;

	public AccessFilterException() {
		super();
	}

	public AccessFilterException(String message) {
		super(message);
	}
}

/**
 * 
 */
package com.github.aiseno.access.exception;

/**
 * @author admin
 */
public class CustomAccessException extends AccessFilterException {

	private static final long serialVersionUID = 3175834973963284490L;

	private String path;
	
	public CustomAccessException(String path,String msg) {
		super(msg);
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}

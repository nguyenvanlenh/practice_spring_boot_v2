package com.watermelon.controller.exception;

public class JwtValidationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JwtValidationException(String message) {
        super(message);
    }
}

package com.crypto.coins.exception;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = -6448909780108548405L;

	public ValidationException(String message) {
		super(message);
	}

}

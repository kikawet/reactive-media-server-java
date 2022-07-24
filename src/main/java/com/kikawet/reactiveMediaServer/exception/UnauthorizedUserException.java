package com.kikawet.reactiveMediaServer.exception;

public class UnauthorizedUserException extends RuntimeException {
	public UnauthorizedUserException() {
		super();
	}

	public UnauthorizedUserException(String msg) {
		super(msg);
	}
}

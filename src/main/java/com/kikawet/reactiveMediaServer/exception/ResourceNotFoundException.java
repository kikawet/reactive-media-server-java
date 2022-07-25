package com.kikawet.reactiveMediaServer.exception;

public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String resourceName) {
		super("Unable to find resource " + resourceName);
	}

	public ResourceNotFoundException(Exception e) {
		super(e);
	}
}

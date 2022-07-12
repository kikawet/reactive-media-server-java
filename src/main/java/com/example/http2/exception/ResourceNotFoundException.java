package com.example.http2.exception;

import org.springframework.core.io.Resource;

public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(Resource resource) {
		super("Unable to access resource " + resource.getFilename());
	}

	public ResourceNotFoundException(Exception e) {
		super(e);
	}
}

package com.example.http2.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HandlerBadRequests {

	@ExceptionHandler({ IOException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handlerBadRequests() {
	}
}

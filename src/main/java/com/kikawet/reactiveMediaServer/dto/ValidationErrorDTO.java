package com.kikawet.reactiveMediaServer.dto;

import org.springframework.validation.FieldError;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationErrorDTO {	
	String message;
	String field;

	public ValidationErrorDTO(final FieldError fe) {
		this.field = fe.getField();
		this.message = fe.getDefaultMessage();
	}
}

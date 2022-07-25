package com.kikawet.reactiveMediaServer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationErrorListDTO<T> {
	T item;
	List<ValidationErrorDTO> errors;
}

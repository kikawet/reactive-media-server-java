package com.kikawet.reactiveMediaServer.validator;

import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

public abstract class AbstactRequestValidationHandler<T, U extends Validator> {

	private final Class<T> validationClass;
	private final U validator;

	protected AbstactRequestValidationHandler(Class<T> clazz, U validator) {
		this.validationClass = clazz;
		this.validator = validator;
	}

	public Mono<ServerResponse> requireValidBody(
			ServerRequest request,
			Function<T, Mono<ServerResponse>> success,
			Function<Errors, Mono<ServerResponse>> error) {

		Mono<T> monoo = request
				.bodyToMono(this.validationClass);

		return monoo
				.switchIfEmpty(Mono
						.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body cannot be empty.")))
				.flatMap(
						body -> {
							Errors errors = new BeanPropertyBindingResult(
									body,
									this.validationClass.getName());

							this.validator.validate(body, errors);

							if (errors == null || errors.getAllErrors().isEmpty()) {
								return success.apply(body);
							} else {
								return error.apply(errors);
							}
						});
	}

	public Mono<ServerResponse> requireValidBody(
			ServerRequest request,
			Function<T, Mono<ServerResponse>> success) {
		return this.requireValidBody(request, success,
				errors -> {
					throw new ResponseStatusException(
							HttpStatus.BAD_REQUEST,
							errors.getAllErrors().toString());
				});
	}
}

package com.kikawet.reactiveMediaServer.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.kikawet.reactiveMediaServer.dto.ValidationErrorDTO;
import com.kikawet.reactiveMediaServer.dto.ValidationErrorListDTO;

import reactor.core.publisher.Mono;

public abstract class AbstactRequestValidationHandler<T, U extends Validator> {

	private final Class<T> validationClass;
	private final U validator;

	protected AbstactRequestValidationHandler(final Class<T> clazz, final U validator) {
		this.validationClass = clazz;
		this.validator = validator;
	}

	public Mono<ServerResponse> requireValidBodyList(
			final ServerRequest request,
			final Function<Collection<T>, Mono<ServerResponse>> success,
			final BiFunction<ServerRequest, Map<T, Errors>, Mono<ServerResponse>> error) {

		return request.bodyToFlux(this.validationClass)
				.reduce(new HashMap<T, Errors>(), (validationMap, item) -> {
					Errors errors = validateBody(item);
					validationMap.put(item, errors);
					return validationMap;
				})
				.flatMap(validationMap -> {
					if (validationMap.values().stream().allMatch(e -> e == null || e.getAllErrors().isEmpty())) {
						return success.apply(validationMap.keySet());
					}

					return error.apply(request, validationMap);

				});
	}

	public Mono<ServerResponse> requireValidBodyList(
			final ServerRequest request,
			final Function<Collection<T>, Mono<ServerResponse>> success) {

		return this.requireValidBodyList(request, success, this::defaultErrorListHandler);
	}

	public Mono<ServerResponse> requireValidBody(
			final ServerRequest request,
			final Function<T, Mono<ServerResponse>> success,
			final BiFunction<ServerRequest, Errors, Mono<ServerResponse>> error) {

		return request
				.bodyToMono(this.validationClass)
				.switchIfEmpty(Mono
						.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body cannot be empty.")))
				.flatMap(
						body -> {
							Errors errors = this.validateBody(body);

							if (errors == null || errors.getAllErrors().isEmpty()) {
								return success.apply(body);
							} else {
								return error.apply(request, errors);
							}
						});
	}

	public Mono<ServerResponse> requireValidBody(
			final ServerRequest request,
			final Function<T, Mono<ServerResponse>> success) {
		return this.requireValidBody(request, success, this::defaultErrorHandler);
	}

	protected Mono<ServerResponse> defaultErrorHandler(final ServerRequest request, final Errors errors) {
		request.attributes().put("errors", errors
				.getFieldErrors().stream()
				.map(ValidationErrorDTO::new));
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"Validation Error: invalid " + this.validationClass.getName());
	}

	protected Mono<ServerResponse> defaultErrorListHandler(
			final ServerRequest request,
			final Map<T, Errors> validationMap) {

		request.attributes().put("errors",
				validationMap.entrySet().stream()
						.filter(entry -> entry.getValue().hasErrors())
						.map(entry -> new ValidationErrorListDTO<T>(
								entry.getKey(),
								entry.getValue().getFieldErrors().stream().map(ValidationErrorDTO::new).toList())));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"Validation error: invalid array of " + this.validationClass.getName());
	}

	protected Errors validateBody(final T body) {
		Errors errors = new BeanPropertyBindingResult(body, this.validationClass.getName());

		this.validator.validate(body, errors);

		return errors;
	}
}

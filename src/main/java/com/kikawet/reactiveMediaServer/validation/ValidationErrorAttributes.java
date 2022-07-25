package com.kikawet.reactiveMediaServer.validation;

import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class ValidationErrorAttributes extends DefaultErrorAttributes {
	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
		Map<String, Object> map = super.getErrorAttributes(request, options);

		var optErrors = request.attribute("errors");
		optErrors.ifPresent(errors -> map.put("errors", errors));

		map.values().removeIf(Objects::isNull);

		return map;
	}
}

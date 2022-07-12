package com.example.http2.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class DefaultRouter {

	@Bean
	RouterFunction<ServerResponse> redirectRoute() {
		return route(GET("/"), req -> ServerResponse.temporaryRedirect(URI.create("/video"))
				.build());
	}
}

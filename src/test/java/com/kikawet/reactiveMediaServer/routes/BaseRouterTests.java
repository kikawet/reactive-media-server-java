package com.kikawet.reactiveMediaServer.routes;

import java.time.Duration;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

public abstract class BaseRouterTests {

	protected WebTestClient getWebTestClient(final RouterFunction<?> route) {
		return WebTestClient.bindToRouterFunction(route)
				.configureClient()
				.responseTimeout(Duration.ofMinutes(1))
				.build();
	}
}

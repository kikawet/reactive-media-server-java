package com.kikawet.reactiveMediaServer.routes;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

public abstract class BaseRouterTests {

	protected WebTestClient getWebTestClient(RouterFunction<?> route) {
		return WebTestClient.bindToRouterFunction(route).build();
	}
}

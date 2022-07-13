package com.kikawet.reactiveMediaServer.routes;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

public class BaseRouterTests {

	protected final WebTestClient client;

	public BaseRouterTests(RouterFunction<?> route) {
		this.client = WebTestClient.bindToRouterFunction(route).build();
	}
}

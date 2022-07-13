package com.example.http2.routes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.server.RouterFunction;

@SpringBootTest
public class HomeRouterTests extends BaseRouterTests {

	public HomeRouterTests(@Autowired RouterFunction<?> redirectRoute) {
		super(redirectRoute);
	}

	@Test
	void redirectRouteTest() {
		client.get()
				.uri("/")
				.exchange()
				.expectStatus()
				.is3xxRedirection()
				.expectHeader()
				.location("/video");
	}
}

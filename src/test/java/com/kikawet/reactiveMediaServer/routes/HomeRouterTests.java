package com.kikawet.reactiveMediaServer.routes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.server.RouterFunction;

@SpringBootTest
public class HomeRouterTests extends BaseRouterTests {

	@Test
	void redirectRouteTest(@Autowired RouterFunction<?> redirectRoute) {
		getWebTestClient(redirectRoute)
				.get()
				.uri("/")
				.exchange()
				.expectStatus()
				.is3xxRedirection()
				.expectHeader()
				.location("/video");
	}
}

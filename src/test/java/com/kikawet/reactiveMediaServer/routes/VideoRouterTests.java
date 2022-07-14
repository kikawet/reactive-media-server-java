package com.kikawet.reactiveMediaServer.routes;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.kikawet.reactiveMediaServer.service.VideoService;

@SpringBootTest
public class VideoRouterTests extends BaseRouterTests {

	@MockBean
	VideoService vs;

	private final List<String> videosList = Arrays.asList("video1", "video2", "video3");

	@BeforeEach
	void setUp() {
		when(vs.findAllVideoTitle()).thenReturn(videosList.stream());
	}

	@Test
	void findAllVideoTitleRouteTest(@Autowired RouterFunction<?> findAllVideoTitleRoute) {
		getWebTestClient(findAllVideoTitleRoute)
				.get()
				.uri("/video")
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody(List.class)
				.isEqualTo(videosList);
	}
}

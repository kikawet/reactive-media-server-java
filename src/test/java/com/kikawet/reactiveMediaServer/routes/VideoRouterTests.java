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
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.kikawet.reactiveMediaServer.service.VideoService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class VideoRouterTests extends BaseRouterTests {

	@MockBean
	VideoService vs;

	private final List<String> videosList = Arrays.asList("video1", "video2", "video3");

	@BeforeEach
	void setUp() {
		when(vs.findAllVideos()).thenReturn(videosList.stream());
	}

	@Test
	void AllVideosTests(@Autowired RouterFunction<?> findAllVideosRoute) {
		FluxExchangeResult<String> result = getWebTestClient(findAllVideosRoute)
				.get()
				.uri("/video")
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.returnResult(String.class);

		Flux<String> eventFlux = result.getResponseBody();

		String expect = StringUtils.collectionToDelimitedString(videosList, ",","\"","\"");

		expect = "[" + expect + "]";

		StepVerifier.create(eventFlux)
				.expectNext(expect)
				.verifyComplete();
	}

}

package com.kikawet.reactiveMediaServer.routes;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.kikawet.reactiveMediaServer.exception.ResourceNotFoundException;
import com.kikawet.reactiveMediaServer.service.VideoService;

@SpringBootTest
public class VideoRouterTests extends BaseRouterTests {

	@MockBean
	VideoService vs;

	private final List<String> videosList = Arrays.asList("video1", "video2", "video3");
	private final Resource mockVideo = new ClassPathResource("application.yml");

	@BeforeEach
	void setUp() {
		when(vs.findAllVideoTitle()).thenReturn(videosList.stream());
		when(vs.findVideoByName(anyString())).thenThrow(ResourceNotFoundException.class);
		when(vs.findVideoByName(eq("mockVideo"))).thenReturn(mockVideo);
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

	@Test
	void findVideoByNameRouteTest(@Autowired RouterFunction<?> findVideoByNameRoute) throws IOException {
		getWebTestClient(findVideoByNameRoute)
				.get()
				.uri("/video/mockVideo")
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentType("video/yml")
				.expectBody(byte[].class)
				.isEqualTo(mockVideo.getInputStream().readAllBytes());
	}

	@Test
	void findVideoByNameRouteThrowsTest(@Autowired RouterFunction<?> findVideoByNameRoute) throws IOException {
		getWebTestClient(findVideoByNameRoute)
				.get()
				.uri("/video/unknown")
				.exchange()
				.expectStatus()
				.isNotFound()
				.expectBody()
				.isEmpty();
	}
}

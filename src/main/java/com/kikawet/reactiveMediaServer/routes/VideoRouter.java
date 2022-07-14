package com.kikawet.reactiveMediaServer.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kikawet.reactiveMediaServer.exception.ResourceNotFoundException;
import com.kikawet.reactiveMediaServer.service.VideoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class VideoRouter {

	@Autowired
	private VideoService videos;

	@Bean
	RouterFunction<ServerResponse> findAllVideoTitleRoute() {
		return route(GET("/video"), req -> ok()
				.body(Flux.fromStream(videos.findAllVideoTitle()).collectList(), List.class));
	}

	@Bean
	RouterFunction<ServerResponse> findVideoByNameRoute() {
		return route(GET("/video/{name}"), this::findVideoByNameHandler);
	}

	public Mono<ServerResponse> findVideoByNameHandler(ServerRequest serverRequest) {
		final String name = serverRequest.pathVariable("name");

		try {
			final Resource video = videos.findVideoByName(name);
			final String mimeType = StringUtils.getFilenameExtension(video.getFilename().toString());

			if (!video.exists())
				return notFound().build();

			return ok()
					.contentType(MediaType.asMediaType(MimeType.valueOf("video/" + mimeType)))
					.body(Mono.just(video), Resource.class);
		} catch (ResourceNotFoundException rnfe) {
			return notFound().build();
		}
	}
}

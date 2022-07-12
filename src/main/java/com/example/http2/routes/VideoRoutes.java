package com.example.http2.routes;

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

import com.example.http2.exception.ResourceNotFoundException;
import com.example.http2.service.VideoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class VideoRoutes {

	@Autowired
	private VideoService videos;

	@Bean
	RouterFunction<ServerResponse> getAllRoute() {
		return route(GET("/video"), req -> ok()
				.body(Flux.fromStream(videos.getAllVideos()).collectList(), List.class));
	}

	@Bean
	RouterFunction<ServerResponse> getVideoByNameRoute() {
		return route(GET("/video/{name}"), this::getVideoByNameHandler);
	}

	public Mono<ServerResponse> getVideoByNameHandler(ServerRequest serverRequest) {
		final String name = serverRequest.pathVariable("name");

		try {
			final Resource video = videos.getVideoByName(name);
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

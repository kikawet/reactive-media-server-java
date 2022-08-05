package com.kikawet.reactiveMediaServer.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kikawet.reactiveMediaServer.beans.PageableMapper;
import com.kikawet.reactiveMediaServer.exception.ResourceNotFoundException;
import com.kikawet.reactiveMediaServer.model.Video;
import com.kikawet.reactiveMediaServer.service.VideoService;

import reactor.core.publisher.Mono;

@Configuration
public class VideoRouter {

	@Autowired
	private VideoService videos;

	@Autowired
	private PageableMapper pm;

	@Bean
	RouterFunction<ServerResponse> findAllVideoTitleRoute() {
		return route(GET("/video"), req -> ok()
				.body(videos.findAllVideoTitle(pm.getPageable(req)).collectList(),
						List.class));
	}

	@Bean
	RouterFunction<ServerResponse> findVideoByNameRoute() {
		return route(GET("/video/{name}"), this::findVideoByNameHandler);
	}

	@Bean
	RouterFunction<ServerResponse> createVideo() {
		return route(POST("/video/{name}"), req -> {
			final String name = req.pathVariable("name");
			return status(HttpStatus.CREATED).body(videos.createVideo(name), Video.class);
		});
	}

	public Mono<ServerResponse> findVideoByNameHandler(final ServerRequest serverRequest) {
		final String name = serverRequest.pathVariable("name");

		try {
			final Resource video = videos.findVideoByName(name);
			final String mimeType = StringUtils.getFilenameExtension(video.getFilename().toString());

			return ok()
					.contentType(MediaType.asMediaType(MimeType.valueOf("video/" + mimeType)))
					.body(Mono.just(video), Resource.class);
		} catch (final ResourceNotFoundException rnfe) {
			return notFound().build();
		}
	}
}

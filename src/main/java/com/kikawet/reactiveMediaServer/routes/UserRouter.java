package com.kikawet.reactiveMediaServer.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static reactor.core.publisher.Flux.fromStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.kikawet.reactiveMediaServer.beans.PageableMapper;
import com.kikawet.reactiveMediaServer.dto.WatchedVideoDTO;
import com.kikawet.reactiveMediaServer.exception.ResourceNotFoundException;
import com.kikawet.reactiveMediaServer.exception.UnauthorizedUserException;
import com.kikawet.reactiveMediaServer.model.WatchedVideo;
import com.kikawet.reactiveMediaServer.service.UserService;
import com.kikawet.reactiveMediaServer.validation.WatcherVideoDTOValidationHandler;

import reactor.core.publisher.Mono;

@Configuration
public class UserRouter {
	@Autowired
	private UserService users;

	@Autowired
	private PageableMapper pm;

	@Autowired
	private WatcherVideoDTOValidationHandler watcherValidationHandler;

	@Bean
	RouterFunction<ServerResponse> getHistoryByLogin() {
		return route(GET("/user/{login}/history"), this::getHistoryByLoginHandler);
	}

	@Bean
	RouterFunction<ServerResponse> updateHistoryByLogin() {
		return route(POST("/user/{login}/watch"), this::updateHistoryByLoginHandler)
				.and(route(POST("/user/{login}/history"), this::updateHistoryByLoginHandler));
	}

	public Mono<ServerResponse> updateHistoryByLoginHandler(ServerRequest serverRequest) {
		final String login = serverRequest.pathVariable("login");

		return watcherValidationHandler.requireValidBodyList(serverRequest,
				newWatches -> {
					if (newWatches.isEmpty())
						return ok().build();

					return users.updateHistoryByLogin(login, newWatches)
							.flatMap(success -> {
								if (success)
									return status(HttpStatus.CREATED).build();
								return badRequest().build();
							})
							.onErrorResume(UnauthorizedUserException.class,
									e -> status(HttpStatus.UNAUTHORIZED).build())
							.doOnError(ResourceNotFoundException.class, e -> {
								throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
							});

				});
	}

	public Mono<ServerResponse> getHistoryByLoginHandler(ServerRequest serverRequest) {
		final String login = serverRequest.pathVariable("login");
		final Pageable page = pm.getPageable(serverRequest);

		return users.getHistoryByLogin(login, page)
				.map(history -> history.map(WatchedVideo::toDTO))
				.flatMap(dtos -> ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromStream(dtos), WatchedVideoDTO.class))
				.onErrorResume(UnauthorizedUserException.class, e -> status(HttpStatus.UNAUTHORIZED).build());
	}
}

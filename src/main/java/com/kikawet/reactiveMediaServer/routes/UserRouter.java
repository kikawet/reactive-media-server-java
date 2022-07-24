package com.kikawet.reactiveMediaServer.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static reactor.core.publisher.Flux.fromStream;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kikawet.reactiveMediaServer.beans.PageableMapper;
import com.kikawet.reactiveMediaServer.dto.WatchedVideoDTO;
import com.kikawet.reactiveMediaServer.exception.UnauthorizedUserException;
import com.kikawet.reactiveMediaServer.model.WatchedVideo;
import com.kikawet.reactiveMediaServer.service.UserService;

import reactor.core.publisher.Mono;

@Configuration
public class UserRouter {
	@Autowired
	private UserService users;

	@Autowired
	private PageableMapper pm;

	@Bean
	RouterFunction<ServerResponse> getHistoryByLogin() {
		return route(GET("/user/{login}/history"), this::getHistoryByLoginHandler);
	}

	public Mono<ServerResponse> getHistoryByLoginHandler(ServerRequest serverRequest) {
		final String login = serverRequest.pathVariable("login");
		final Pageable page = pm.getPageable(serverRequest);

		try {
			Stream<WatchedVideoDTO> history = users.getHistoryByLogin(login, page).map(WatchedVideo::toDTO);

			return ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(fromStream(history),
							List.class);
		} catch (UnauthorizedUserException e) {
			return status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}

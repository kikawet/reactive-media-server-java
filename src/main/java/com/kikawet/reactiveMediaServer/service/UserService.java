package com.kikawet.reactiveMediaServer.service;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kikawet.reactiveMediaServer.beans.PageableMapper;
import com.kikawet.reactiveMediaServer.dto.WatchedVideoDTO;
import com.kikawet.reactiveMediaServer.exception.UnauthorizedUserException;
import com.kikawet.reactiveMediaServer.model.User;
import com.kikawet.reactiveMediaServer.model.WatchedVideo;
import com.kikawet.reactiveMediaServer.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository users;

	@Autowired
	private VideoService videoService;

	public Mono<Stream<WatchedVideo>> getHistoryByLogin(String login) {
		return this.getHistoryByLogin(login, PageableMapper.DEFAULT_PAGE_REQUEST);
	}

	public Mono<Stream<WatchedVideo>> getHistoryByLogin(String login, Pageable page) {
		return validateLogin(login).map(user ->

		user.getHistory()
			.skip(page.getOffset())
			.limit(page.getPageSize()));
	}

	public Mono<Boolean> updateHistoryByLogin(String login, Collection<WatchedVideoDTO> newWatches) {
		return validateLogin(login).flatMap(user ->

		Flux.concat(newWatches.stream().map(dto -> {
			return videoService.findVideoByTitle(dto.getTitle()).map(video -> {
				WatchedVideo wv = new WatchedVideo();

				wv.setUser(user);
				wv.setVideo(video);
				wv.setTime(dto.getTime());
				wv.setCompletionPercentage(dto.getCompletionPercentage());

				return user.appendHistory(wv);
			});

		}).toList())
				.takeUntil(x -> !x)
				.reduce(true, (accum, value) -> accum && value));
	}

	private Mono<User> validateLogin(String login) {
		Mono<User> u = users.findByLogin(login);

		if (u == null) {
			// throw new UnauthorizedUserException();
			return Mono.error(new UnauthorizedUserException());
		}

		return u;
	}
}

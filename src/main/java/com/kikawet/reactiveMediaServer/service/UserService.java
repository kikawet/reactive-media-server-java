package com.kikawet.reactiveMediaServer.service;

import java.util.Collection;

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
import reactor.core.scheduler.Scheduler;

@Service
public class UserService {

	@Autowired
	private UserRepository users;

	@Autowired
	private VideoService videoService;

	@Autowired
	private Scheduler scheduler;

	public Flux<WatchedVideo> getHistoryByLogin(final String login) {
		return this.getHistoryByLogin(login, PageableMapper.DEFAULT_PAGE_REQUEST);
	}

	public Flux<WatchedVideo> getHistoryByLogin(final String login, final Pageable page) {
		return validateLogin(login)
				.map(user -> user.getHistory()
						.skip(page.getOffset())
						.limit(page.getPageSize()))
				.flatMapMany(Flux::fromStream);
	}

	public Mono<Boolean> updateHistoryByLogin(final String login, final Collection<WatchedVideoDTO> newWatches) {
		return validateLogin(login).flatMap(user ->

		Flux.concat(newWatches.stream().map(dto -> {
			return videoService.findVideoByTitle(dto.getTitle()).map(video -> {
				final WatchedVideo wv = new WatchedVideo();

				wv.setUser(user);
				wv.setVideo(video);
				wv.setTime(dto.getTime());
				wv.setCompletionPercentage(dto.getCompletionPercentage());

				return user.addWatchVideo(wv);
			});

		}).toList())
				.takeUntil(x -> !x)
				.reduce(true, (accum, value) -> accum && value));
	}

	private Mono<User> validateLogin(final String login) {
		return Mono.fromCallable(() -> users.findByLogin(login))
				.publishOn(scheduler)
				.switchIfEmpty(Mono.error(new UnauthorizedUserException()));
	}
}

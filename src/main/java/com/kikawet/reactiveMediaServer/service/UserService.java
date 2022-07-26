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

	public Stream<WatchedVideo> getHistoryByLogin(String login) {
		return this.getHistoryByLogin(login, PageableMapper.DEFAULT_PAGE_REQUEST);
	}

	public Stream<WatchedVideo> getHistoryByLogin(String login, Pageable page) {
		User user = validateLogin(login);
		return user.getHistory()
				.skip(page.getOffset())
				.limit(page.getPageSize());
	}

	public Mono<Boolean> updateHistoryByLogin(String login, Collection<WatchedVideoDTO> newWatches) {
		User user = validateLogin(login);

		return Flux.concat(newWatches.stream().map(dto -> {
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
				.reduce(true, (accum, value) -> accum && value);
	}

	private User validateLogin(String login) {
		User u = users.findById(login);

		if (u == null) {
			throw new UnauthorizedUserException();
		}

		return u;
	}
}

package com.kikawet.reactiveMediaServer.service;

import java.util.Collection;
import java.util.stream.Collectors;
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

	public boolean updateHistoryByLogin(String login, Collection<WatchedVideoDTO> newWatches) {
		User user = validateLogin(login);

		boolean result = user.appendHistory(newWatches.stream().map(dto -> {
			WatchedVideo wv = new WatchedVideo();

			wv.setUser(user);
			wv.setVideo(videoService.findVideoByTitle(dto.getTitle()).block());// TODO: remove this block ...
			wv.setTime(dto.getTime());
			wv.setCompletionPercentage(dto.getCompletionPercentage());

			return wv;
		}).collect(Collectors.toList()));

		return result;
	}

	private User validateLogin(String login) {
		User u = users.findById(login);

		if (u == null) {
			throw new UnauthorizedUserException();
		}

		return u;
	}
}

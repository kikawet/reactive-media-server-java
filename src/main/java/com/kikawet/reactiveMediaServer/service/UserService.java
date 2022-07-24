package com.kikawet.reactiveMediaServer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kikawet.reactiveMediaServer.beans.PageableMapper;
import com.kikawet.reactiveMediaServer.exception.UnauthorizedUserException;
import com.kikawet.reactiveMediaServer.model.User;
import com.kikawet.reactiveMediaServer.model.Video;
import com.kikawet.reactiveMediaServer.model.WatchedVideo;

@Service
public class UserService {
	private final Map<String, User> users;

	public UserService(Map<String, User> users) {
		super();
		this.users = users;
	}

	@PostConstruct
	void setUp(){
		User u = new User();
		Video v = new Video(":D");

		u.setLogin("tom");

		u.setHistory(List.of(
			new WatchedVideo(u, v, LocalDateTime.now(), 17),
			new WatchedVideo(u, v, LocalDateTime.now(), 69),
			new WatchedVideo(u, v, LocalDateTime.now(), 33)
		));

		this.users.put(u.getLogin(), u);
	}

	public Stream<WatchedVideo> getHistoryByLogin(String login) {
		return this.getHistoryByLogin(login, PageableMapper.DEFAULT_PAGE_REQUEST);
	}

	public Stream<WatchedVideo> getHistoryByLogin(String login, Pageable page) {
		User user = validateLogin(login);
		return user.getHistory()
				.skip(page.getOffset())
				.limit(page.getPageSize());
	}

	private User validateLogin(String login) {
		User u = users.get(login);

		if (u == null) {
			throw new UnauthorizedUserException();
		}

		return u;
	}
}

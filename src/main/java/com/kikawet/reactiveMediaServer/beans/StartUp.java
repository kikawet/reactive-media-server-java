package com.kikawet.reactiveMediaServer.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.kikawet.reactiveMediaServer.model.User;
import com.kikawet.reactiveMediaServer.model.Video;
import com.kikawet.reactiveMediaServer.model.WatchedVideo;
import com.kikawet.reactiveMediaServer.repository.UserRepository;
import com.kikawet.reactiveMediaServer.repository.VideoRepository;

@Component
@Profile("!test")
public class StartUp {
	// TODO: remove this class when the user repo is done and move everything to sql

	@Autowired
	Environment env;

	@Autowired
	private UserRepository users;

	@Autowired
	private VideoRepository videos;

	@PostConstruct
	void setUp() {
		User u = new User();
		Video v = new Video(":D");

		u.setLogin("tom");

		u.setHistory(new ArrayList<>(
				Arrays.asList(
						new WatchedVideo(u, v, LocalDateTime.now(), 17),
						new WatchedVideo(u, v, LocalDateTime.now(), 69),
						new WatchedVideo(u, v, LocalDateTime.now(), 33))));

		this.users.save(u);

		User user = this.users.findByLogin(u.getLogin())
		.map(usr -> {
			System.out.println("User " + usr.getLogin() + " already in database");
			return usr;
		})
		.switchIfEmpty(this.users.save(u))
		.block();
		
		System.out.println("Saved user " + user.getLogin());

		Video video = this.videos.findByTitle(v.getTitle())
		.map(vid -> {
			System.out.println("Video " + vid.getTitle() + " already in database");
			return vid;
		})
		.switchIfEmpty(this.videos.save(v))
		.block();
		
		System.out.println("Saved video " + video.getTitle());
	}
}

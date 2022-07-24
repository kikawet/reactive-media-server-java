package com.kikawet.reactiveMediaServer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	String login;
	List<Video> availableVideos = new ArrayList<>();
	List<WatchedVideo> history = new ArrayList<>();

	public Stream<Video> getVideos() {
		return availableVideos.stream();
	}

	public Stream<WatchedVideo> getHistory() {
		return history.stream();
	}
}

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
	List<Video> avaiableVideos = new ArrayList<>();
	List<WatchedVideo> history = new ArrayList<>();

	public Stream<Video> getVideos() {
		return avaiableVideos.stream();
	}

	public Stream<WatchedVideo> getHistory() {
		return history.stream();
	}
}

package com.kikawet.reactiveMediaServer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

	public boolean appendHistory(Collection<WatchedVideo> history) {
		return history.addAll(history);
	}
}

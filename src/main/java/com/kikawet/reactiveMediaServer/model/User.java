package com.kikawet.reactiveMediaServer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
	@NonNull
	String login;

	List<Video> availableVideos = new ArrayList<>();
	List<WatchedVideo> history = new ArrayList<>();

	public Stream<Video> getVideos() {
		return availableVideos.stream();
	}

	public Stream<WatchedVideo> getHistory() {
		return history.stream();
	}

	public boolean appendHistory(WatchedVideo wv) {
		return this.history.add(wv);
	}
}

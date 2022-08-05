package com.kikawet.reactiveMediaServer.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Entity
@Table(name = "USERS") // User is a keyword so can't create a table with that name
public class User {
	@Id
	@NonNull
	String login;

	@OneToMany
	Set<Video> availableVideos = new HashSet<>();

	@Transient
	List<WatchedVideo> history = new ArrayList<>();

	public Stream<Video> getAvailableVideos() {
		return availableVideos.stream();
	}

	public Stream<WatchedVideo> getHistory() {
		return history.stream();
	}

	public boolean addWatchVideo(WatchedVideo wv) {
		if (!availableVideos.contains(wv.video))
			return false;

		return this.history.add(wv);
	}
}

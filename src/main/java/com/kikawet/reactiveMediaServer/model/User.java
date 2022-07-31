package com.kikawet.reactiveMediaServer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
@Table("USERS")
public class User {
	@Id
	@Column("ID_USER")
	Long id;

	@NonNull
	@Column("LOGIN_USER")
	String login;

	@Transient
	List<Video> availableVideos = new ArrayList<>();
	@Transient
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

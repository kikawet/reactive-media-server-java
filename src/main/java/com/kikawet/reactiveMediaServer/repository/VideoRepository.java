package com.kikawet.reactiveMediaServer.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kikawet.reactiveMediaServer.model.Video;

@Repository
public class VideoRepository {
	private static final Map<String, Video> videos = new HashMap<>();

	public Video findVideoByTitle(String title) {
		return videos.get(title);
	}

	public void put(String title, Video video) {
		videos.put(title, video);
	}
}

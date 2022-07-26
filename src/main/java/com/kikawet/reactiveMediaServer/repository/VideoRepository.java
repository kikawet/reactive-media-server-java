package com.kikawet.reactiveMediaServer.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.kikawet.reactiveMediaServer.model.Video;

import reactor.core.publisher.Mono;

public interface VideoRepository extends ReactiveCrudRepository<Video, Long>{
	Mono<Video> findByTitle(String title);
	Mono<Video> save(Video video);
}

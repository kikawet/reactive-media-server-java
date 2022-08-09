package com.kikawet.reactiveMediaServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kikawet.reactiveMediaServer.model.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
	Video findByTitle(String title);
}

package com.kikawet.reactiveMediaServer.model;

import java.time.LocalDateTime;

import com.kikawet.reactiveMediaServer.dto.WatchedVideoDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WatchedVideo {
	User user;
	Video video;
	LocalDateTime time;
	float completionPercentage;

	public WatchedVideoDTO toDTO() {
		WatchedVideoDTO dto = new WatchedVideoDTO();

		dto.login = user.login;
		dto.title = video.title;
		dto.time = time;
		dto.completionPercentage = completionPercentage;

		return dto;
	}
}

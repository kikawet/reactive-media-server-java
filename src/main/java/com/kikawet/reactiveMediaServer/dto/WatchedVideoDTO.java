package com.kikawet.reactiveMediaServer.dto;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
public class WatchedVideoDTO {
	public String login;
	public String title;
	public LocalDateTime time;
	public float completionPercentage;
}

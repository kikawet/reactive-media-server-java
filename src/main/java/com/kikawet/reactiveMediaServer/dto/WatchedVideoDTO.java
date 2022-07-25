package com.kikawet.reactiveMediaServer.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class WatchedVideoDTO {
	@NotNull
	public String login;
	@NotNull
	public String title;
	@PastOrPresent
	@NotNull
	public LocalDateTime time;
	@Min(0)
	@Max(100)
	public float completionPercentage;
}

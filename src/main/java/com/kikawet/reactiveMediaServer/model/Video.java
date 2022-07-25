package com.kikawet.reactiveMediaServer.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Video {
	@Id
	String title;
}

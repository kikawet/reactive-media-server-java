package com.kikawet.reactiveMediaServer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor
public class Video {
	@Id
	@NonNull
	String title;
}

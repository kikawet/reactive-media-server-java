package com.kikawet.reactiveMediaServer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
@Table("VIDEO")
@RequiredArgsConstructor
@NoArgsConstructor
public class Video {
	@Id
	@Column("ID_VIDEO")
	Long id;

	@Column("TITLE")
	@NonNull
	String title;
}

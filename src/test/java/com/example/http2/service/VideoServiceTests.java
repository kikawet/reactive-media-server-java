package com.example.http2.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VideoServiceTests {

	@Autowired
	private VideoService vService;

	private static final List<String> videoExtensions = Arrays.asList(".webm", ".mp4");

	@Test
	void getAllVideosTest() throws IOException {
		assertThat(vService.getAllVideos()).isNotEmpty()
				.allSatisfy(videoName -> assertThat(videoExtensions.stream()
						.filter(extension -> videoName.contains((extension)))
						.collect(Collectors.toUnmodifiableList())).isEmpty());
	}

	@Test
	void findVideoByName() throws IOException {
		final String videoName = vService.getAllVideos().get(0);
		assertThat(vService.getVideoPathFromName(videoName)).isNotEmpty()
				.hasValueSatisfying(path -> path.getFileName().toString().contains(videoName));
	}
}

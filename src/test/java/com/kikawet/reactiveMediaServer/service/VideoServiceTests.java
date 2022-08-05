package com.kikawet.reactiveMediaServer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.kikawet.reactiveMediaServer.beans.PageableMapper;
import com.kikawet.reactiveMediaServer.beans.VideoResourceLoader;
import com.kikawet.reactiveMediaServer.exception.ResourceNotFoundException;

import reactor.test.StepVerifier;

@SpringBootTest
public class VideoServiceTests {

	@Autowired
	VideoService vService;

	@MockBean
	VideoResourceLoader resourceLoader;

	@Value("${video.basePath}")
	private Path videoBasePath;

	private static final List<String> videoExtensions = Arrays.asList(".webm", ".mp4");
	private static final List<Path> videoList = IntStream.rangeClosed(1, 20)
			.mapToObj(n -> String.format("video%d%s", n, selectRandom(videoExtensions)))
			.map(Paths::get)
			.collect(Collectors.toUnmodifiableList());

	static <T> T selectRandom(List<T> list) {
		int index = (int) (Math.random() * list.size());
		return list.get(index);
	}

	@BeforeEach
	void setUp() {
		when(resourceLoader.listFiles(videoBasePath)).thenReturn(videoList.stream());
		when(resourceLoader.getResource(anyString()))
				.thenAnswer(i -> new FileSystemResource(i.getArgument(0).toString()));
	}

	@Test
	void findAllVideosTest() {
		StepVerifier.create(vService.findAllVideoTitle())
				.thenConsumeWhile(videoName -> videoExtensions.stream()
						.filter(ext -> videoName.contains(ext))
						.collect(Collectors.toUnmodifiableList())
						.isEmpty())
				.expectComplete();
	}

	@Test
	void findAllVideosPaginationTest() {
		StepVerifier.create(vService.findAllVideoTitle())
				.expectNextCount(PageableMapper.DEFAULT_PAGE_REQUEST.getPageSize())
				.expectComplete();
	}

	@Test
	void findVideoByNameTest() {
		assertThat(vService.findVideoByName("video1")).isNotNull().extracting(Resource::getFilename)
				.extracting(Paths::get).isIn(videoList);
	}

	@Test
	void findVideoByNameThrowsTest() {
		assertThrows(ResourceNotFoundException.class, () -> vService.findVideoByName("video0"));
	}
}

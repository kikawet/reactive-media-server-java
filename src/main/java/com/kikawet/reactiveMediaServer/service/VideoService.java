package com.kikawet.reactiveMediaServer.service;

import static org.springframework.util.StringUtils.stripFilenameExtension;

import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kikawet.reactiveMediaServer.beans.PageableMapper;
import com.kikawet.reactiveMediaServer.beans.VideoResourceLoader;
import com.kikawet.reactiveMediaServer.exception.ResourceNotFoundException;
import com.kikawet.reactiveMediaServer.model.Video;
import com.kikawet.reactiveMediaServer.repository.VideoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class VideoService {

	@Value("${video.basePath}")
	private Path videoBasePath;

	@Autowired
	private VideoResourceLoader resourceLoader;

	@Autowired
	private VideoRepository videos;

	@Autowired
	private Scheduler scheduler;

	public Mono<Video> findVideoByTitle(final String title) {
		return Mono.fromCallable(() -> videos.findByTitle(title))
				.publishOn(scheduler)
				.switchIfEmpty(Mono.error(new ResourceNotFoundException(title)));
	}

	public Resource findVideoByName(final String fileName) {
		Optional<Path> videoPath = resourceLoader
				.listFiles(videoBasePath)
				.parallel()
				.filter(file -> stripFilenameExtension(file.getFileName().toString())
						.equals(fileName))
				.findAny();

		if (!videoPath.isPresent()) {
			throw new ResourceNotFoundException();
		}

		return resourceLoader.getResource(videoPath.get().toString());
	}

	public Flux<String> findAllVideoTitle(final Pageable page) {
		return Flux.fromStream(resourceLoader.listFiles(videoBasePath)
				.skip(page.getOffset())
				.limit(page.getPageSize())
				.map(Path::getFileName)
				.map(Path::toString)
				.map(StringUtils::stripFilenameExtension));
	}

	public Flux<String> findAllVideoTitle() {
		return this.findAllVideoTitle(PageableMapper.DEFAULT_PAGE_REQUEST);
	}

	public Mono<Video> createVideo(final String title) {
		return this.findVideoByTitle(title)
				.onErrorResume(e -> Mono.fromCallable(() -> this.videos.saveAndFlush(new Video(title))))
				.publishOn(scheduler);

	}
}

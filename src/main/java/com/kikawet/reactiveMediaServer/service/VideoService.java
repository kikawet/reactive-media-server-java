package com.kikawet.reactiveMediaServer.service;

import static org.springframework.util.StringUtils.stripFilenameExtension;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

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

@Service
public class VideoService {

	@Value("${video.basePath}")
	private Path videoBasePath;

	@Autowired
	private VideoResourceLoader resourceLoader;

	@Autowired
	private VideoRepository videos;

	public Video findVideoByTitle(String title) {
		Video v = videos.findVideoByTitle(title);

		if (v == null) {
			throw new ResourceNotFoundException(title);
		}

		return v;
	}

	public Resource findVideoByName(String fileName) {
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

	public Stream<String> findAllVideoTitle(final Pageable page) {
		return resourceLoader.listFiles(videoBasePath)
				.skip(page.getOffset())
				.limit(page.getPageSize())
				.map(Path::getFileName)
				.map(Path::toString)
				.map(StringUtils::stripFilenameExtension);
	}

	public Stream<String> findAllVideoTitle() {
		return this.findAllVideoTitle(PageableMapper.DEFAULT_PAGE_REQUEST);
	}
}

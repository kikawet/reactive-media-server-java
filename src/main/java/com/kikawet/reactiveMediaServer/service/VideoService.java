package com.kikawet.reactiveMediaServer.service;

import static org.springframework.util.StringUtils.stripFilenameExtension;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kikawet.reactiveMediaServer.beans.VideoResourceLoader;
import com.kikawet.reactiveMediaServer.exception.ResourceNotFoundException;
import com.kikawet.reactiveMediaServer.record.Page;

@Service
public class VideoService {

	@Value("${video.basePath}")
	private Path videoBasePath;
	public static final Page defaulPagination = Page.from(1, 5);

	@Autowired
	private VideoResourceLoader resourceLoader;

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

	public Stream<String> findAllVideos(final Page page) {
		return resourceLoader.listFiles(videoBasePath)
				.skip(page.skip())
				.limit(page.limit())
				.map(Path::getFileName)
				.map(Path::toString)
				.map(StringUtils::stripFilenameExtension);
	}

	public Stream<String> findAllVideos() {
		return this.findAllVideos(defaulPagination);
	}
}

package com.example.http2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.http2.exception.ResourceNotFoundException;
import com.example.http2.record.Page;

@Service
public class VideoService {

	@Value("${video.basePath}")
	private Path videoBasePath;
	private static final Page defaulPagination = Page.from(1, 5);

	@Autowired
	private ResourceLoader resourceLoader;

	public Resource getVideoByName(String fileName) {
		Optional<Path> videoPath = Optional.empty();

		try (Stream<Path> stream = Files.list(videoBasePath)) {
			videoPath = stream.parallel()
					.filter(file -> StringUtils.stripFilenameExtension(file.getFileName().toString())
							.equals(fileName))
					.findAny();
		} catch (IOException e) {
			throw new RuntimeException("Unable to access videos folder", e);
		}

		if (!videoPath.isPresent()) {
			throw new ResourceNotFoundException();
		}

		return this.resourceLoader.getResource("file:" + videoPath.get());
	}

	public Stream<String> getAllVideos(final Page page) {
		try {
			return Files.list(videoBasePath)
					.filter(file -> !Files.isDirectory(file))
					.skip(page.skip())
					.limit(page.limit())
					.map(Path::getFileName)
					.map(Path::toString)
					.map(StringUtils::stripFilenameExtension);
		} catch (IOException e) {
			throw new InternalError("Unable to access videos folder", e);
		}
	}

	public Stream<String> getAllVideos() {
		return this.getAllVideos(defaulPagination);
	}
}

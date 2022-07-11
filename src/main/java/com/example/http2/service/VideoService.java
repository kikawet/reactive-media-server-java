package com.example.http2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.http2.record.Page;

@Service
public class VideoService {

	@Value("${video.basePath}")
	private Path videoBasePath;
	private static final Page defaulPagination = Page.from(1, 5);

	public Optional<Path> getVideoPathFromName(String fileName) throws IOException {
		try (Stream<Path> stream = Files.list(videoBasePath)) {
			return stream.parallel()
					.filter(file -> StringUtils.stripFilenameExtension(file.getFileName().toString())
							.equals(fileName))
					.findAny();
		}
	}

	public List<String> getAllVideos(final Page page) throws IOException {
		try (Stream<Path> stream = Files.list(videoBasePath)) {
			return stream
					.filter(file -> !Files.isDirectory(file))
					.skip(page.skip())
					.limit(page.limit())
					.map(Path::getFileName)
					.map(Path::toString)
					.map(StringUtils::stripFilenameExtension)
					.collect(Collectors.toUnmodifiableList());
		}
	}

	public List<String> getAllVideos() throws IOException {
		return this.getAllVideos(defaulPagination);
	}
}

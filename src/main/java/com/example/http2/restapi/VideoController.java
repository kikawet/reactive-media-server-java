package com.example.http2.restapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.http2.service.VideoService;

@RestController
@RequestMapping("video")
public class VideoController {

	@Autowired
	private VideoService videos;

	@GetMapping
	public List<String> getAllVideos() throws IOException {
		return videos.getAllVideos();
	}

	@GetMapping("/{partialName}")
	public ResponseEntity<StreamingResponseBody> getVideo(@PathVariable String partialName) throws IOException {
		Optional<Path> videoPathOpt = videos.getVideoPathFromName(partialName);

		if (!videoPathOpt.isPresent())
			return ResponseEntity.notFound().build();

		Path videoPath = videoPathOpt.get();

		StreamingResponseBody responseBody = outputStream -> {
			Files.copy(videoPath, outputStream);
		};

		String mimeType = StringUtils.getFilenameExtension(videoPath.getFileName().toString());

		return ResponseEntity.ok()
				.contentType(MediaType.asMediaType(MimeType.valueOf("video/" + mimeType)))
				.body(responseBody);
	}
}

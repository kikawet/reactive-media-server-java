package com.example.http2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VideoService {

    @Value("${video.basePath}")
    private Path videoBasePath;

    public List<String> getAllVideos() throws IOException {
        try (Stream<Path> stream = Files.list(videoBasePath)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(VideoService::removeExtension)
                    .collect(Collectors.toUnmodifiableList());
        }
    }

    private static String removeExtension(String filename) {
        return filename.split("\\.")[0];
    }

}

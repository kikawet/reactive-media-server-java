package com.kikawet.reactiveMediaServer.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class VideoResourceLoader extends FileSystemResourceLoader {

	@Override
	public Resource getResource(String path) {
		return super.getResource(path);
	}

	public Stream<Path> listFiles(Path path) {
		try {
			return Files.list(path)
					.parallel()
					.filter(file -> !Files.isDirectory(file));
		} catch (IOException e) {
			throw new RuntimeException("Unable to access: " + path.getFileName().toString(), e);
		}
	}

	public Stream<Path> listFiles(String path) {
		return listFiles(Paths.get(path));
	}

	@Override
	public ClassLoader getClassLoader() {
		return super.getClassLoader();
	}

}

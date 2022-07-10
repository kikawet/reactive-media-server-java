package com.example.http2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.example.http2.restapi", "com.example.http2.service",
		"com.example.http2.exception"
})
public class Http2Application {

	public static void main(String[] args) {
		SpringApplication.run(Http2Application.class, args);
	}

}

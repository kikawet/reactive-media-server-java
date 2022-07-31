package com.kikawet.reactiveMediaServer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kikawet.reactiveMediaServer.exception.UnauthorizedUserException;
import com.kikawet.reactiveMediaServer.model.User;
import com.kikawet.reactiveMediaServer.repository.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class UserServiceTests {

	@Autowired
	UserService uService;

	@MockBean
	UserRepository users;

	@BeforeEach
	void setUp() {
		User u = new User();
		u.setLogin("test");

		when(users.findByLogin(u.getLogin())).thenReturn(Mono.just(u));
	}

	@Test
	void getHistoryByLoginThrowsTest() {
		StepVerifier.create(uService.getHistoryByLogin("test"))
				.assertNext(history -> assertThat(history).isNotNull())
				.expectComplete();

		StepVerifier.create(uService.getHistoryByLogin("null"))
				.expectError(UnauthorizedUserException.class)
				.verify();
	}
}

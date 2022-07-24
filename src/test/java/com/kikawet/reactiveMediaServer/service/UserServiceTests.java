package com.kikawet.reactiveMediaServer.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.kikawet.reactiveMediaServer.exception.UnauthorizedUserException;
import com.kikawet.reactiveMediaServer.model.User;

@SpringBootTest
public class UserServiceTests {

	UserService uService;

	@BeforeEach
	void setUp() {
		var testUsers = getTestUsers();
		uService = new UserService(testUsers);
	}

	Map<String, User> getTestUsers() {
		HashMap<String, User> testUsers = new HashMap<>();

		User u = new User();
		u.setLogin("test");

		testUsers.put(u.getLogin(), u);
		return testUsers;
	}

	@Test
	void getHistoryByLoginThrowsTest() {
		assertThat(uService.getHistoryByLogin("test")).doesNotContainNull();
		assertThrows(UnauthorizedUserException.class, () -> uService.getHistoryByLogin("null"));
	}
}

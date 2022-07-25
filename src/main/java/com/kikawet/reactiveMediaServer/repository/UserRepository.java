package com.kikawet.reactiveMediaServer.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kikawet.reactiveMediaServer.model.User;

@Repository
public class UserRepository {
	private static final Map<String, User> users = new HashMap<>();

	public User findById(String login) {
		return users.get(login);
	}

	public void updateUser(User u) {
		users.put(u.getLogin(), u);
	}

	public void put(String login, User u) {
		users.put(login, u);
	}
}

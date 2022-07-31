package com.kikawet.reactiveMediaServer.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.kikawet.reactiveMediaServer.model.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long>{
	Mono<User> findByLogin(String login);

	// public User findById(String login) {
	// 	return users.get(login);
	// }

	// public void updateUser(User u) {
	// 	users.put(u.getLogin(), u);
	// }

	// public void put(String login, User u) {
	// 	users.put(login, u);
	// }
}

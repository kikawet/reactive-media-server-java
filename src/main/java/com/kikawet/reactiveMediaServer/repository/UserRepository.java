package com.kikawet.reactiveMediaServer.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kikawet.reactiveMediaServer.model.User;

import reactor.core.publisher.Mono;

@Repository
public class UserRepository { // extends JpaRepository<User, Long>{
	static final Map<String, User> users = new HashMap<>();
	
	//Mono<User> findByLogin(String login);

	public Mono<User> findByLogin(String login){
		return Mono.just(users.get(login));
	}

	public User save(User u){
		users.put(u.getLogin(), u);
		return u;
	}

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

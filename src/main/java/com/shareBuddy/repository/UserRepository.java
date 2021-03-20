package com.shareBuddy.repository;

import org.springframework.data.repository.CrudRepository;

import com.shareBuddy.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findByPhone(String phone);
	
	User findById(int uid);
}

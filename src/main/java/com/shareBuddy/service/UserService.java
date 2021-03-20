package com.shareBuddy.service;

import java.util.Set;

import com.shareBuddy.entities.User;
import com.shareBuddy.entities.security.PasswordResetToken;
import com.shareBuddy.entities.security.UserRole;

public interface UserService {
	
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail(String email);

	User createUser(User user, Set<UserRole> userRoles) throws Exception;

	User save(User user);
	 
 
	User findById(int id);

	User findByPhone(String userPhone);
}

package com.shareBuddy;

import java.util.HashSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.shareBuddy.entities.User;
import com.shareBuddy.entities.security.Role;
import com.shareBuddy.entities.security.UserRole;
import com.shareBuddy.service.UserService;
import com.shareBuddy.utility.SecurityUtility;

 
@SpringBootApplication
public class ShareBuddyApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ShareBuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstName("Prasanna");
		user1.setLastName("Phadtare");
		user1.setUsername("prasanna");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("prasanna"));
		user1.setEmail("prasannaphadtare9876@gmail.com");
		
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		userService.createUser(user1, userRoles);

	}

}

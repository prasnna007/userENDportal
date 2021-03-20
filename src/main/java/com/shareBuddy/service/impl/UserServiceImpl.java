package com.shareBuddy.service.impl;
 
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shareBuddy.entities.WishCart;
import com.shareBuddy.entities.User;
import com.shareBuddy.entities.security.PasswordResetToken;
import com.shareBuddy.entities.security.UserRole;
import com.shareBuddy.repository.PasswordResetTokenRepository;
import com.shareBuddy.repository.RoleRepository; 
import com.shareBuddy.repository.UserRepository; 
import com.shareBuddy.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	 
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		
		return passwordResetTokenRepository.findByToken(token) ;
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return  userRepository.findByEmail(email);
	}

	@Override
	public User findByPhone(String phone) {
		return  userRepository.findByPhone(phone);
	}
	
	
	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		User localeUser = userRepository.findByUsername(user.getUsername());
		if(localeUser != null) {
			LOG.info("User {} already exists. Nothing will be done.", user.getUsername());
			
		}else {
			for( UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			
			user.getUserRoles().addAll(userRoles);
			
			WishCart shoppingCart = new WishCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			 
			
			localeUser = userRepository.save(user);
		}
		
		
		return localeUser;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findById(int id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	
}

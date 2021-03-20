package com.shareBuddy.utility; 

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtility {
	private static final String psd = "psd"; //This should be very complicated and protected at all times.  

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(psd.getBytes()));
	}
	
	@Bean
	public static String randomPassword() {
		String PSKCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789@#$%^&*()_+-";
		StringBuilder psd = new StringBuilder();
		Random rnd = new Random();
		
		while(psd.length()<8) {
			int index = (int) (rnd.nextFloat()*PSKCHARS.length());
			psd.append(PSKCHARS.charAt(index));
		}
		String pskStr = psd.toString();
		return pskStr;
	}
}



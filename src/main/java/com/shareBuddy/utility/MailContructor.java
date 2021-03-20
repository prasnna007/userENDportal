package com.shareBuddy.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.Message;
import com.shareBuddy.entities.Order;
import com.shareBuddy.entities.User;

@Component
public class MailContructor {
	
	@Autowired
	private Environment env;

	@Autowired
	private TemplateEngine templateEngine;

	public SimpleMailMessage constructResetTokenMail(String contextPath, Locale locale, String token, User user, String password) {
		
		String url = contextPath+"/newAccount?token="+token;
		String message = "\n Please click on this to Reset Your password is: \n "+password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject(" ShareBuddy Account's Password Reset Token ");
		email.setText(url+message);
		email.setFrom(env.getProperty("support.mail"));
		return email;
	}
	
	
	
	public MimeMessagePreparator constructInterestEmail(Message msg,Book bk,User user, User userPurchaser,Order order, Locale english) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("bk", bk);
		context.setVariable("msg", msg);
		context.setVariable("userPurchaser", userPurchaser);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderInterestedEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Interested Buyer Offer For Your Book - "+bk.getTitle());
				email.setText(text, true);
				email.setFrom(new InternetAddress("dream.fello.12.12@gmail.com"));
			}
		};
		
		return messagePreparator;
	}
}

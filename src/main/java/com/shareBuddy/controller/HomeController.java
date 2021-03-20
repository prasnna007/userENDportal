package com.shareBuddy.controller;

import java.security.Principal;
import java.util.Arrays; 
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.User;
import com.shareBuddy.entities.security.Role;
import com.shareBuddy.entities.security.UserRole;
import com.shareBuddy.service.BookService; 
import com.shareBuddy.service.UserService;  
import com.shareBuddy.utility.MailContructor;
import com.shareBuddy.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailContructor mailConstructor;

	@Autowired
	private UserService userService;
 

	@Autowired
	private BookService bookService;
 

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	
	 
	

	@RequestMapping("/forgotPassword")
	public String forgotPassword(HttpServletRequest request, @ModelAttribute("email") String userEmail, Model model)
			throws Exception {
		model.addAttribute("classActiveForgotPassword", true);
		User user = userService.findByEmail(userEmail);
		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		userService.save(user);

		String token = UUID.randomUUID().toString();
		
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenMail(appUrl, request.getLocale(), token, user,
				password);
		mailSender.send(email);
		model.addAttribute("forgotPasswordEmailSent", true);
		return "myAccount";
	}

	
	@RequestMapping(value = "/newAccount", method = RequestMethod.POST)
	public String newAccountPost(Locale locale, HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("firstName") String userFirstName,@ModelAttribute("lastName") String userLastName,
			@ModelAttribute("Phone") String userPhone,
			@ModelAttribute("password") String userpwd,			
			@ModelAttribute("username") String username, Model model) throws Exception {
		model.addAttribute("ClassActiveNewAccount", true);
		model.addAttribute("username", username);
		model.addAttribute("email", userEmail);
		model.addAttribute("firstName", userFirstName);
		model.addAttribute("lastName", userLastName);
		model.addAttribute("Phone", userPhone);
		 
		
		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			return "myAccount";
		}

		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			return "myAccount";
		}
		
		if (userService.findByPhone(userPhone) != null) {
			model.addAttribute("phoneExists", true);
			return "myAccount";
		}
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);
		user.setFirstName(userFirstName);
		user.setLastName(userLastName);
		user.setPhone(userPhone);
				
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(userpwd);
		user.setPassword(encryptedPassword);

		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		
		Set<UserRole> userRoles = new HashSet<>();
		
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles); 

		model.addAttribute("createdSuccesfully", true);

		return "accSuccesful";
	}


	@RequestMapping("/bookshelf")
	private String bookhelf(Model model) {
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "bookshelf";
	}

	@RequestMapping("/bookDetail")
	public String bookDetail(@PathParam("id") Integer id, Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		Book book = bookService.findOne(id);
		model.addAttribute("book", book);
		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);

		return "bookDetail";
	}

	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		 
		model.addAttribute("classActiveEdit", true);

		return "myProfile";
	}

	 
	
	
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute("user") User user, Principal principal, Model model) {
		  
		model.addAttribute("user", user);
 		return "myProfile";
	}

	
	 
	
	@RequestMapping("/changepwd")
	public String getupdatepwd(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "changepwd";
	}
	
	

	@RequestMapping(value="/changepwd", method=RequestMethod.POST)
	public String updatepwd(@ModelAttribute("user") User user,@ModelAttribute("newPassword") String newPassword,
			Model model
			) throws Exception {
		
		User currentUser = userService.findByUsername(user.getUsername());
		System.out.println(user.getUsername());
		
		if(currentUser == null) {
			throw new Exception ("User with this username not found");
		}
		
		/*check email already exists*/
		if (userService.findByEmail(user.getEmail())!=null) {
			if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				return "myProfile";
			}
		}
	 
		// update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if(passwordEncoder.matches(user.getPassword(), dbPassword)){
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				
				return "myProfile";
			}
		}
		
		userService.save(currentUser);
		
		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);
		
		return "changepwd";
	}
	

}

package com.shareBuddy.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shareBuddy.entities.ReqBook;
import com.shareBuddy.entities.User;
import com.shareBuddy.service.BookService;
import com.shareBuddy.service.ReqBookService;
import com.shareBuddy.service.UserService;

@Controller
@RequestMapping("/requestBook")
public class RequestBookController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReqBookService reqbookService;
	
	@RequestMapping("/addRequest")
	public String addBook(Model model,Principal principal) {
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		ReqBook book = new ReqBook();
		model.addAttribute("reqbook", book);
		return "addRequest";
	}
	

	@RequestMapping("/deletereqBook")
	public String delBook( @RequestParam ("id") int bid,Model model) {
		
		ReqBook reqbook = reqbookService.findOne(bid);
		reqbookService.remove(reqbook);
		
		return "redirect:/requestBook/viewRequests";
	}
	
	@RequestMapping("/viewRequests")
	public String viewRequestsBook(Model model,Principal principal) {
		if(principal!=null) {
			List<ReqBook> rbooks;
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
			rbooks=reqbookService.getAllBooksByReqUserId(user.getId());
			model.addAttribute("rbooks", rbooks);	
			}
		return "viewRequests";
	}
	
	
	@RequestMapping("/reqbooksInfo")
	public String viewRequestBookInfo(@RequestParam("id") Integer id,Model model) {
	
			ReqBook reqbook = reqbookService.findOne(id);
			model.addAttribute("reqbook",reqbook);
			return "reqbooksInfo";
	}
	
	
	@RequestMapping(value="/addRequest", method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("reqbook") ReqBook book, HttpServletRequest request, Model model,Principal principal
			) {
		
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user_id", user.getId());
			book.setReqUserid(user.getId()); 
			System.out.println(user.getId());
			book.setUser(user); 
		}
			model.addAttribute("isReqAdded",true);
			bookService.save(book);
			System.out.println(book.getId());
			System.out.println(book.toString());
			
		MultipartFile bookImage = book.getBookImage();
		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getId()+".png";
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new java.io.File("C:/Users/iampr/Desktop/Image/Uploads"+name))); //ADMIN SIDE
 			stream.write(bytes);
			stream.close();
			
			BufferedOutputStream stream1 = new BufferedOutputStream(new FileOutputStream(new java.io.File("src/main/resources/static/images/book/"+name)));
 			stream1.write(bytes);
			stream1.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "bookAdded";
	}
	
	
}

package com.shareBuddy.controller;

import java.security.Principal; 
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 
import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.CartItem;
import com.shareBuddy.entities.Message;
import com.shareBuddy.entities.Order; 
import com.shareBuddy.entities.WishCart;
import com.shareBuddy.entities.User;
import com.shareBuddy.service.BookService;
import com.shareBuddy.service.CartItemService;
import com.shareBuddy.service.MessageService;
import com.shareBuddy.service.OrderService;
import com.shareBuddy.service.WishCartService;
import com.shareBuddy.service.UserService; 
import com.shareBuddy.utility.MailContructor; 

@Controller
public class OfferDealController {
	 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private BookService bookService;
	
	 
	@SuppressWarnings("unused")
	@Autowired
	private WishCartService wishCartService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailContructor mailConstructor;
	 
	 
	@RequestMapping(value = "/offerDeal")
	public String offerDealPost(Model model, Principal principal) {

		System.out.print("inside offer deal");
		User user = userService.findByUsername(principal.getName());

		List<CartItem> cartItemList = cartItemService.findByWishCart(user.getWishCart());
		
		System.out.println(cartItemList.toString());
		
		if(cartItemList.size()==0) {
			model.addAttribute("emptyCart", true);
			return "forward:/wishList/myList";
		}
				
		model.addAttribute("wishCart", user.getWishCart());
		model.addAttribute("cartItemList", cartItemList);
		
		return "offerDeal";
	}
	
	
	@RequestMapping(value = "/offerDeal", method = RequestMethod.POST)
	public String offerDealPost(
			@RequestParam("id")Long cartId,
			@RequestParam(value = "missingRequiredField", required=false) boolean missingRequiredField,
			@RequestParam("id")int id,@RequestParam("ids")int ids,
			@RequestParam("description") String description, 
			Principal principal, Model model) {
	 		
		WishCart wishCart = userService.findByUsername(principal.getName()).getWishCart();

		List<CartItem> cartItemList = cartItemService.findByWishCart(wishCart);
		model.addAttribute("cartItemList", cartItemList);
 
		System.out.println(ids);
		System.out.println(id);
		System.out.println(description);
		Book bk = bookService.findOne(id);
		
		User userSeller = userService.findById(ids);
	 	 
		User userPurchaser = userService.findByUsername(principal.getName());

		if(userSeller.getId() == userPurchaser.getId()) {
		 
			model.addAttribute("purchaseError", true);
			return "forward:/wishList/myList";
		}
		 
		if(cartItemList.size() > 1) {
	 
			model.addAttribute("purchaseErrorForDifferentSeller", true);
			return "forward:/wishList/myList";
		}
	 	
		Order order = orderService.createOrder(wishCart, userSeller);
		
		System.out.println("Seller:= "+ userSeller.getFirstName());
		System.out.println("Purchaser:= "+ userPurchaser.getFirstName());
		System.out.println("Book Details:="+bk.getTitle());
		
		Message msg = new Message();
		 
		msg.setMessage(description);
		msg.setSeller_id(ids);
		msg.setReq_id(userPurchaser.getId());
		
		messageService.save(msg);
		mailSender.send(mailConstructor.constructInterestEmail(msg,bk,userSeller,userPurchaser, order, Locale.ENGLISH));
		  
		return "orderSubmittedPage";
	}
	
	 
}

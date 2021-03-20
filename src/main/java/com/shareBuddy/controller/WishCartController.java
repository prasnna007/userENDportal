package com.shareBuddy.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.CartItem;
import com.shareBuddy.entities.WishCart;
import com.shareBuddy.entities.User;
import com.shareBuddy.service.BookService;
import com.shareBuddy.service.CartItemService;
import com.shareBuddy.service.WishCartService;
import com.shareBuddy.service.UserService;

@Controller
@RequestMapping("/wishList")
public class WishCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private WishCartService wishCartService;
	
	@Autowired
	private BookService bookService;
	
	
	@RequestMapping("/myList")
	public String wishCart(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		WishCart wishCart = user.getWishCart();
		List<CartItem> cartItemList = cartItemService.findByWishCart(wishCart);
		wishCartService.updateWishCart(wishCart);
		
		model.addAttribute("cartItemList",cartItemList);
		model.addAttribute("wishCart",wishCart);
		return "wishList";
	}
	 
	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("book") Book book, @ModelAttribute("qty") String qty, Model model, Principal principal ) {
		
		
		User user = userService.findByUsername(principal.getName());
		book = bookService.findOne(book.getId());
		if(Integer.parseInt(qty)>book.getInStockNumber()) {
			model.addAttribute("notEnoughStock",true);
			return "forward:/bookDetail?id="+book.getId();
		}
		
		CartItem cartItem = cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess",true);
	 	
		
		System.out.println(cartItem.toString());
		return "forward:/bookDetail?id="+book.getId();
	}
	
	@RequestMapping("/updateCartItem")
	public String updateWishList(
			@ModelAttribute("id") Long cartItemId, @ModelAttribute("qty") int qty ) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/wishList/myList";
		
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		return "forward:/wishList/myList";
	}
}

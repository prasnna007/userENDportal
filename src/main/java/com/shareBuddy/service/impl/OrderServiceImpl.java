package com.shareBuddy.service.impl;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.CartItem;
import com.shareBuddy.entities.Order;
import com.shareBuddy.entities.WishCart;
import com.shareBuddy.entities.User;
import com.shareBuddy.repository.OrderRepository;
import com.shareBuddy.service.CartItemService;
import com.shareBuddy.service.OrderService;
 

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	public synchronized Order createOrder(WishCart wishCart,  User user) {
		Order order = new Order(); 
		
		List<CartItem> cartItemList = cartItemService.findByWishCart(wishCart);
		
		for(CartItem cartItem : cartItemList) {
			Book book = cartItem.getBook();
			cartItem.setOrder(order);
			
		order.setCartItemList(cartItemList); 
		order.setOrderTotal(wishCart.getGrandTotal()); 
		order.setUser(user);
		order.setSellerId(book.getSellerId());
		order = orderRepository.save(order);

		}
		 
		return order;
	}
	
	public Order findOne(Long id) {
		return ((OrderService) orderRepository).findOne(id);
	}

}

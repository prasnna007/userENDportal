 package com.shareBuddy.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shareBuddy.entities.CartItem;
import com.shareBuddy.entities.WishCart;
import com.shareBuddy.repository.WishCartRepository;
import com.shareBuddy.service.CartItemService;
import com.shareBuddy.service.WishCartService;

@Service
public class WishCartServiceImpl implements WishCartService {

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private WishCartRepository wishCartRepository;
	
	@Override
	public WishCart updateWishCart(WishCart wishCart) {
		BigDecimal cartTotal = new BigDecimal(0);
		
		List<CartItem> cartItemList = cartItemService.findByWishCart(wishCart); 
		
		for(CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber()>0) {
				cartItemService.updateCartItem(cartItem);
				cartTotal = cartTotal.add(cartItem.getSubTotal());
			}
		}
		
		
		
		wishCart.setGrandTotal(cartTotal);
		
		wishCartRepository.save(wishCart);
		
		return wishCart;
	}

}

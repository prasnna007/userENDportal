package com.shareBuddy.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.BookToCartItem;
import com.shareBuddy.entities.CartItem;
import com.shareBuddy.entities.WishCart;
import com.shareBuddy.entities.User;
import com.shareBuddy.repository.BookToCartItemRepository;
import com.shareBuddy.repository.CartItemRepository;
import com.shareBuddy.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private BookToCartItemRepository bookToCartItemRepository;
	
	@Override
	public List<CartItem> findByWishCart(WishCart wishCart) {
		return cartItemRepository.findByWishCart(wishCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		double price = cartItem.getBook().getOurPrice();
		BigDecimal dec1= BigDecimal.valueOf(price);
		
		BigDecimal bigDecimal = dec1.multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubTotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
	}

	@Override
	public CartItem addBookToCartItem(Book book, User user, int qty) {
		List<CartItem> cartItemList = findByWishCart(user.getWishCart());
		for(CartItem cartItem : cartItemList) {
			if(book.getId()==cartItem.getBook().getId()) {
				cartItem.setQty(cartItem.getQty()+qty);
				BigDecimal ourPrice = BigDecimal.valueOf(book.getOurPrice());
				BigDecimal qtyy = BigDecimal.valueOf(qty);
				cartItem.setSubTotal(ourPrice.multiply(qtyy));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getWishCart());
		cartItem.setBook(book);
		cartItem.setQty(qty);
		BigDecimal ourPrice = BigDecimal.valueOf(book.getOurPrice());
		BigDecimal qtyy = BigDecimal.valueOf(qty);
		cartItem.setSubTotal(ourPrice.multiply(qtyy));
		
		cartItem = cartItemRepository.save(cartItem);
		
		BookToCartItem bookToCartItem = new BookToCartItem();
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemRepository.save(bookToCartItem);
		return cartItem;
	}

	@Override
	public CartItem findById(Long cartItemId) {
		Optional<CartItem> cartItemResponse = cartItemRepository.findById(cartItemId);
		CartItem cartItem = cartItemResponse.get();
		return cartItem;
	}

	@Override
	public void removeCartItem(CartItem cartItem) {
		bookToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}

}

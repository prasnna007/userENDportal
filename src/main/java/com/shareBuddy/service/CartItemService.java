package com.shareBuddy.service;

import java.util.List;

import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.CartItem;
import com.shareBuddy.entities.WishCart;
import com.shareBuddy.entities.User;

public interface CartItemService {

	List<CartItem> findByWishCart(WishCart wishCart);

	CartItem updateCartItem(CartItem cartItem);

	CartItem addBookToCartItem(Book book, User user, int parseInt);

	CartItem findById(Long cartItemId);

	void removeCartItem(CartItem findById);
}

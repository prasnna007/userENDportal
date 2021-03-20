package com.shareBuddy.service;

import com.shareBuddy.entities.Order;
import com.shareBuddy.entities.WishCart;
import com.shareBuddy.entities.User;

public interface OrderService {
	Order createOrder(WishCart shoppingCart, User user);
	Order findOne(Long id);
}

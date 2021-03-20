package com.shareBuddy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.shareBuddy.entities.CartItem;
import com.shareBuddy.entities.WishCart;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

	List<CartItem> findByWishCart(WishCart wishCart);
}

package com.shareBuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.shareBuddy.entities.BookToCartItem;
import com.shareBuddy.entities.CartItem;

@Transactional
public interface BookToCartItemRepository extends  CrudRepository<BookToCartItem, Long>{

	void deleteByCartItem(CartItem cartItem);

}

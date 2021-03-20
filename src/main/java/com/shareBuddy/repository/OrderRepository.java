package com.shareBuddy.repository;

import org.springframework.data.repository.CrudRepository;

import com.shareBuddy.entities.Order;
 

public interface OrderRepository extends CrudRepository<Order, Long>{

}

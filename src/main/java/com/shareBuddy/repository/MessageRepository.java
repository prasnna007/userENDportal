package com.shareBuddy.repository;

import org.springframework.data.repository.CrudRepository;

import com.shareBuddy.entities.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {

}

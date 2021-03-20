package com.shareBuddy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shareBuddy.entities.Book;
import com.shareBuddy.entities.ReqBook;
import com.shareBuddy.entities.User;

public interface BookRepository extends CrudRepository<Book, Integer> {

	List<Book> findByCategory(String category);

	List<Book> findByTitleContaining(String title);

	ReqBook save(ReqBook reqbook);

	User findBySellerId(int sid);

}

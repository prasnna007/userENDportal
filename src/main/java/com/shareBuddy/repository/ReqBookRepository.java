package com.shareBuddy.repository;
 
import java.util.List; 
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shareBuddy.entities.ReqBook;
  
 
 @Repository
public interface ReqBookRepository extends CrudRepository<ReqBook, Integer> {

	List<ReqBook> findByreqUserid(int id);
	
	 
}

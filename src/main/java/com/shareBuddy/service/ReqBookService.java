package com.shareBuddy.service;

import java.util.List;

import com.shareBuddy.entities.ReqBook;
 

public interface ReqBookService {
 
 

	ReqBook findOne(Integer id);

	void remove(ReqBook book);
 
	
	List<ReqBook> findAll();

	ReqBook findByReqUserId(Integer id);

	List<ReqBook> getAllBooksByReqUserId(Integer id);

}

package com.shareBuddy.service.impl;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;

import com.shareBuddy.entities.ReqBook;
import com.shareBuddy.repository.ReqBookRepository;
import com.shareBuddy.service.ReqBookService;
@Service
public class ReqBookServiceImpl implements ReqBookService {

	@Autowired
	private ReqBookRepository reqbookRepository;
	

	
	
	@Override
	public void remove(ReqBook reqbook) {
		reqbookRepository.delete(reqbook); 
	}
 

	@Override
	public ReqBook findOne(Integer id) {
		Optional<ReqBook> bookResponse = reqbookRepository.findById(id);
		ReqBook reqbook = bookResponse.get(); 
		return reqbook;
	}
	
	@Override
	public List<ReqBook> getAllBooksByReqUserId(Integer id) { 
		return (List<ReqBook>)reqbookRepository.findByreqUserid(id);
	}

	@Override
	public List<ReqBook> findAll() { 
		return (List<ReqBook>) reqbookRepository.findAll();
	}


	@Override
	public ReqBook findByReqUserId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

 
 

	


	

	 

}

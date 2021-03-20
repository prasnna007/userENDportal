package com.shareBuddy.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shareBuddy.entities.Message;
import com.shareBuddy.repository.MessageRepository;
import com.shareBuddy.service.MessageService;


@Service
public class MessageServiceImpl implements MessageService {

	
	@Autowired
	private MessageRepository msgRepository;
	
	@Override
	public Message save(Message ms) {
		return msgRepository.save(ms);
	}

}

package com.car.service;

public interface MailService {
	
	boolean sentEmail(String receiverId, String message, String subject);
}

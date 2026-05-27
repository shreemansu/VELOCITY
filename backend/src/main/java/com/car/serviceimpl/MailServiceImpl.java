package com.car.serviceimpl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.car.service.MailService;

import lombok.RequiredArgsConstructor;

@Service

public class MailServiceImpl implements MailService {
	
	private final JavaMailSender mailSender;
	 
	private final String senderId;
	
	////While @Value is used we can't use @RequiredArgsConstructor. So we have to manually add constructors for the fields. This the best practice!!!

	public MailServiceImpl(JavaMailSender mailSender, @Value("${spring.mail.username}") String senderId) {
		this.mailSender = mailSender;
		this.senderId = senderId;
	}

	
	@Override
	public boolean sentEmail(String receiverId, String message, String subject) {
		try {
			SimpleMailMessage mailMessage=new SimpleMailMessage();
			mailMessage.setTo(receiverId);
			mailMessage.setFrom(senderId);
			mailMessage.setSubject(subject);
			mailMessage.setText(message);
			mailSender.send(mailMessage);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}

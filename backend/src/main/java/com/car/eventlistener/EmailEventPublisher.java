package com.car.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.car.events.SimpleMessageEvent;
import com.car.service.MailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailEventPublisher {

	private final MailService mailService;
	
	@EventListener
	@Async
	public void handleSimpleEmailEvent(SimpleMessageEvent event) {
		mailService.sentEmail(event.getReceiverEmail(), event.getSubject(), event.getMessage());
	}
}

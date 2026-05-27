package com.car.util;

import org.springframework.stereotype.Component;

@Component
public class EmailMessageBuilderUtil {
	
	public String emailMessageBuilder(String name, Integer otp) {
		StringBuilder builder=new StringBuilder();
		builder.append("Dear "+name+"\n");
		builder.append("Thank You For Your Interest In VELOCITY.\n");
		builder.append("For Your Account Registration The 6 Digit Otp Is Here :"+otp);
		builder.append("\nNote : OTP Valid Only For One Minute");
		return builder.toString();
	}
	
	public String userRegistrationSuccessBuilder(String name) {
		StringBuilder message=new StringBuilder();
		message.append("Dear "+name+",\n");
		message.append("Thanks for your interst in our services, VELOCITY.\n");
		message.append("You have been register with us.\n");
		message.append("Note: This ia a system generated email. Do not reply\n");
		return message.toString();
	}
}

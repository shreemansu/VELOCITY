package com.car.service;

import com.car.dto.AddUserDto;
import com.car.dto.EmailOtpVerifyDto;
import com.car.entity.User;

public interface UserService {
	
    String initiateUserVerification(AddUserDto userDto);
	
	String finalUserVerificationService(EmailOtpVerifyDto otpDto);
	
	String resendOtpService(String email);
	
	String updateUserByIdService(String email, AddUserDto userDto);
	
	User getUserByIdService(Integer id);
}

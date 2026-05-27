package com.car.service;

import com.car.dto.LoginResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
	
	LoginResponseDto loginWithCredentialsService(String email, String password);
	
	String logoutService(HttpServletRequest request);
}

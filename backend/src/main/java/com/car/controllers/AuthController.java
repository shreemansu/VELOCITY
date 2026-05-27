package com.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car.dto.ApiResponse;
import com.car.dto.LoginResponseDto;
import com.car.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v3/car/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> loginWithCredentialsController(@RequestParam String username,@RequestParam String password){
		LoginResponseDto response=authService.loginWithCredentialsService(username, password);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("String")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logoutController(HttpServletRequest request){
		String response=authService.logoutService(request);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("String")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
}

package com.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car.dto.AddUserDto;
import com.car.service.UserService;



import com.car.dto.ApiResponse;
import com.car.dto.EmailOtpVerifyDto;
import com.car.dto.UserResponseDto;
import com.car.entity.User;
import com.car.mapping.CustomModelMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v2/car/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	private final CustomModelMapper mapper;
	
	@PostMapping("/registration")
	public ResponseEntity<ApiResponse> initiateUserVerificationController(@RequestBody AddUserDto userDto) {
		String response=userService.initiateUserVerification(userDto);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("string")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping("/verification")
	public ResponseEntity<ApiResponse> finalUserVerificationController(@RequestBody EmailOtpVerifyDto otpVerifyDto){
		String response=userService.finalUserVerificationService(otpVerifyDto);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("string")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping("/resendOtp")
	public ResponseEntity<ApiResponse> resendOtpController(@RequestParam String email){
		String response=userService.resendOtpService(email);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("Integer")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
	@PutMapping("/{email}")
	public ResponseEntity<ApiResponse> updateUserByIdController(@PathVariable("email") String email,@RequestBody AddUserDto userDto){
		String response=userService.updateUserByIdService(email,userDto);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("Object")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getUserByIdController(@PathVariable("id") Integer id){
		User user=userService.getUserByIdService(id);
		UserResponseDto response=mapper.userResponseDto(user);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("Object")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
}

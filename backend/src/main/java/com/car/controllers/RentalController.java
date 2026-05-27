package com.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car.dto.ApiResponse;
import com.car.service.RentalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v4/car/rental")
@RequiredArgsConstructor
public class RentalController {
	
	private final RentalService rentalService;
	
	@PostMapping("rent")
	public ResponseEntity<ApiResponse> rentCarController(@RequestParam Integer userId,@RequestParam Integer carId){
		String response=rentalService.rentCarService(userId, carId);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("string")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
	@PostMapping("return")
	public ResponseEntity<ApiResponse> returnCarController(@RequestParam Integer rentalId){
		String response=rentalService.returnCarService(rentalId);
		ApiResponse apiResponse=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("string")
				.payload(response)
				.build();
		return ResponseEntity.ok(apiResponse);
	}
}

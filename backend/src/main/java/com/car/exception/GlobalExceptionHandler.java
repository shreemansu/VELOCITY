package com.car.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.car.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> generalExceptionHandler(Exception ex){
		ApiResponse response=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(false)
				.type("string")
				.payload(ex.getMessage())
				.build();
		return ResponseEntity.internalServerError().body(response);
	}
}

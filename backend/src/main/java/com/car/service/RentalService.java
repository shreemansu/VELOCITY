package com.car.service;

import com.car.entity.Rental;

public interface RentalService {
	
	String rentCarService(Integer userId, Integer carId);
	
	String returnCarService(Integer rentalId);
}

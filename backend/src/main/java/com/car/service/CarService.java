package com.car.service;

import java.util.List;

import com.car.dto.CarDto;
import com.car.dto.CarResponseDto;
import com.car.entity.Car;

public interface CarService {
	
	String addNewCarDataService(CarDto carDto);
	
	CarResponseDto getCarByCarIdService(Integer id);
	
	void deleteCarByIdService(Integer id);
	
	List<Car> showAllCarByBrandService(String brand);
	
	List<Car> showAllCarService();
}

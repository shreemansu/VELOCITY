package com.car.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car.dto.ApiResponse;
import com.car.dto.CarDto;
import com.car.dto.CarResponseDto;
import com.car.entity.Car;
import com.car.mapping.CustomModelMapper;
import com.car.service.CarService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
public class CarController {
	
	private final CarService carService;
	
	private final CustomModelMapper customMapper;
	
	private final ModelMapper mapper;
	
	@PostMapping
	public ResponseEntity<ApiResponse> addNewCarDataController(@RequestBody @Valid CarDto carDto) {
		String serviceResponse=carService.addNewCarDataService(carDto);
		ApiResponse response=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("string")
				.payload(serviceResponse)
				.build();
		return ResponseEntity.ok(response);
	}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getCarDataByIdController(@PathVariable("id") Integer id){
		CarResponseDto carResponse=carService.getCarByCarIdService(id);
		ApiResponse response=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("object")
				.payload(carResponse)
				.build();
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteCarByIdController(@PathVariable("id") Integer id){
		carService.deleteCarByIdService(id);
		ApiResponse response=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("object")
				.payload("Car is deleted!")
				.build();
		return ResponseEntity.ok(response);
	}
	@GetMapping("/brand/{brand}")
	public ResponseEntity<ApiResponse> showAllCarByBrandController(@PathVariable("brand") String brand){
		List<Car> cars=carService.showAllCarByBrandService(brand);
		List<CarResponseDto> dtos=cars.stream().map(c->customMapper.carResponseDtoMapper(c)).toList();
		ApiResponse response=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("Object Array")
				.payload(dtos)
				.build();
		return ResponseEntity.ok(response);
	}
	@GetMapping("/showAllCar")
	public ResponseEntity<ApiResponse> showAllCarController(){
		List<Car> cars=carService.showAllCarService();
		List<CarResponseDto> dtos=cars.stream().map(all->customMapper.carResponseDtoMapper(all)).toList();
		ApiResponse response=ApiResponse.builder()
				.serviceName("VELOCITY")
				.status(true)
				.type("Object Array")
				.payload(dtos)
				.build();
		return ResponseEntity.ok(response);
	}
}

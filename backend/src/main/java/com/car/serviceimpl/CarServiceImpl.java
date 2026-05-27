package com.car.serviceimpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.car.dto.CarDto;
import com.car.dto.CarResponseDto;
import com.car.entity.Car;
import com.car.enums.CarStatus;
import com.car.mapping.CustomModelMapper;
import com.car.repository.CarRepository;
import com.car.service.CarService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
	
	private final CarRepository carRepo;
	
	private final CustomModelMapper customMapper;
	
	private final String CACHE_NAME="car";
	
	private final ModelMapper mapper;

	@Override
	@Transactional
	public String addNewCarDataService(CarDto carDto) {
		Car car=mapper.map(carDto, Car.class);
		car=carRepo.save(car);
		return "Car saved in inventory with id "+car.getId();
	}

	@Override
	@Cacheable(value = CACHE_NAME, key = "#id")
	public CarResponseDto getCarByCarIdService(Integer id) {
		Optional<Car> optCar=carRepo.findById(id);
		if(optCar.isEmpty()) throw new NoSuchElementException("No product is found by id "+id);
		Car car=optCar.get();
		//Car car=carRepo.findById(id).orElseThrow(()-> new RuntimeException("No product is found by id "+id));
		//if(car.getStatus()!=CarStatus.AVAILABLE) throw new NoSuchElementException("This car is currently rented by another customer");
		return customMapper.carResponseDtoMapper(car);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = CACHE_NAME, key = "#id")
	public void deleteCarByIdService(Integer id) {
		Optional<Car> optCar=carRepo.findById(id);
		if(optCar.isEmpty()) throw new NoSuchElementException("No product is found by id "+id);
		Car car=optCar.get();
		if(car.getStatus()!=CarStatus.AVAILABLE) throw new NoSuchElementException("This car is under maintainance or Rented by other customer");
		car.setStatus(CarStatus.MAINTAINANCE);
		car=carRepo.save(car);
	}

	@Override
	@Cacheable(value = CACHE_NAME, key = "#brand")
	public List<Car> showAllCarByBrandService(String brand) {
		brand=brand.toUpperCase();
		List<Car> allCar=carRepo.showAllCarByBrand(brand);
		allCar=allCar.stream().filter(i->i.getStatus()==CarStatus.AVAILABLE).toList();
		return allCar;
	}

	@Override
	public List<Car> showAllCarService() {
		List<Car> showAllCar=carRepo.findAll();
		//showAllCar=showAllCar.stream().filter(i->i.getStatus()==CarStatus.AVAILABLE).toList();
		return showAllCar;
	}
}

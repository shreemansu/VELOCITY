package com.car.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.car.entity.Car;
import com.car.entity.Rental;
import com.car.entity.User;
import com.car.enums.CarStatus;
import com.car.enums.RentalStatus;
import com.car.repository.CarRepository;
import com.car.repository.RentalRepository;
import com.car.repository.UserRepository;
import com.car.service.RentalService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
	
	private final CarRepository carRepo;
	
	private final UserRepository userRepo;
	
	private final RentalRepository rentalRepo;

	@Override
	@Transactional
	public String rentCarService(Integer userId, Integer carId) {
		User user=userRepo.findById(userId).orElseThrow();
		Car car=carRepo.findById(carId).orElseThrow();
		if(car.getStatus()==CarStatus.RENTED) throw new RuntimeException("Oops! "+car.getBrand()+" "+car.getModel()+" is currently rented by another customer. Please choose another model");
		if(car.getStatus()==CarStatus.MAINTAINANCE) throw new RuntimeException("Oops! "+car.getBrand()+" "+car.getModel()+" is currently under Maintainance");
		Rental rental=new Rental();
		rental.setRentedAt(LocalDateTime.now());
		rental.setCar(car);
		rental.setUser(user);
		car.setStatus(CarStatus.RENTED);
		rental=rentalRepo.save(rental);
		return user.getName()+" has rented "+car.getBrand()+" "+car.getModel();
	}

	@Override
	@Transactional
	public String returnCarService(Integer rentalId) {
		Rental rental=rentalRepo.findById(rentalId).orElseThrow();
		Long days=ChronoUnit.DAYS.between(rental.getRentedAt(), LocalDateTime.now());
		BigDecimal total=rental.getCar().getPricePerday().multiply(BigDecimal.valueOf(days));
		rental.setTotalAmount(total);
		rental.setReturnedAt(LocalDateTime.now());
		rental.setStatus(RentalStatus.RETURNED);
		rental.getCar().setStatus(CarStatus.AVAILABLE);
		rental=rentalRepo.save(rental);
		return rental.getUser().getName()+" has returned "+rental.getCar().getBrand()+" "+rental.getCar().getModel();
	}
	
}

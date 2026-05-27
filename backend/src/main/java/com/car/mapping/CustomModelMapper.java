package com.car.mapping;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.car.dto.AddUserDto;
import com.car.dto.CarDto;
import com.car.dto.CarResponseDto;
import com.car.dto.UserResponseDto;
import com.car.entity.Car;
import com.car.entity.User;
import com.car.enums.CarStatus;
import com.car.enums.UserStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomModelMapper {
	
	private final PasswordEncoder encoder;
	
	public Car getCarFromAddCarDtoMapper(CarDto carDto) {
		Car car=Car.builder()
				.brand(carDto.getBrand())
				.model(carDto.getModel())
				.plateNumber(carDto.getPlateNumber())
				.pricePerday(carDto.getPricePerday())
				.status(CarStatus.AVAILABLE)
				.build();
		return car;
	}
	
	public CarDto entityToCarDtoMapper(Car car) {
		return CarDto.builder()
				.brand(car.getBrand())
				.model(car.getModel())
				.plateNumber(car.getPlateNumber())
				.pricePerday(car.getPricePerday())
				.status(car.getStatus())
				.build();
	}
	
	public CarResponseDto carResponseDtoMapper(Car car) {
		return CarResponseDto.builder()
				.carId(car.getId())
				.brand(car.getBrand())
				.model(car.getModel())
				.plateNumber(car.getPlateNumber())
				.pricePerday(car.getPricePerday())
				.status(car.getStatus())
				.build();
	}
	
	public User addUserDtoToUserEntity(AddUserDto userDto) {
		User user=User.builder()
				.name(userDto.getName())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.password(encoder.encode(userDto.getPassword()))
				.role("USER")
				.status(UserStatus.ACTIVE)
				.createdAt(LocalDateTime.now())
				.build();
		return user;
	}
	
	public UserResponseDto userResponseDto(User user) {
		return UserResponseDto.builder()
				.email(user.getEmail())
				.name(user.getName())
				.phone(user.getPhone())
				//.password(user.getPassword())
				.build();
	}
}

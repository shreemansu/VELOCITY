package com.car.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.car.enums.CarStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto implements Serializable {
	private String model;
	private String brand;
	private String plateNumber;
	private BigDecimal pricePerday;
	private CarStatus status;
}

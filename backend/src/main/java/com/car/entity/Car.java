package com.car.entity;


import java.math.BigDecimal;
import java.util.List;

import com.car.enums.CarStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car{
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	@Size(min=3, max = 15, message = "Length must be more than 2")
	private String brand;
	
	@Column(nullable = false)
	@Size(min=1, max = 15, message = "Length must be more than 1")
	private String model;
	
	@Size(min=8, max = 15, message = "Length must be more than 8")
	@Column(nullable = false, unique = true)
	private String plateNumber;
	
	@PositiveOrZero(message = "Car price can't be negative")
	@Column(nullable = true)
	private BigDecimal pricePerday;
	
	@Enumerated(EnumType.STRING)
	private CarStatus status=CarStatus.AVAILABLE;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
	private List<Rental> rentals;
	
}

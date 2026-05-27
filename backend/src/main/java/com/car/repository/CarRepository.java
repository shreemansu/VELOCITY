package com.car.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.car.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
	
	@Query("select c from Car c where c.brand= :brand")
	List<Car> showAllCarByBrand(@Param("brand") String brand);
}

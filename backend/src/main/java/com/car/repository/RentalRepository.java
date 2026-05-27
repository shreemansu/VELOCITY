package com.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.entity.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer>{

}

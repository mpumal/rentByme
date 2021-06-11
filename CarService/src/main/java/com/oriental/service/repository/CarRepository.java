package com.oriental.service.repository;

import com.oriental.service.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

    List<Car> findAll();
    List<Car> findByCarBrandName(String carBrandName);

    Car findByCarNumber(String carNumber);

    @Query("SELECT carBrandName FROM Car")
    List<String> findAllCarBrandName();
}

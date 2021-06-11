package com.oriental.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Car;
import com.oriental.service.model.CarImages;
import com.oriental.service.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping(value = "/car")
    public APIResponse saveCar(@RequestParam("imageOne") MultipartFile multipartFileOne, @RequestParam("imageTwo") MultipartFile multipartFileTwo, @RequestParam("imageThree") MultipartFile multipartFileThree, @RequestParam("car") String car) throws IOException {
        Car updatedCar = new ObjectMapper().readValue(car, Car.class);

        CarImages carImages = new CarImages();
        carImages.setImageOne(multipartFileOne.getBytes());
        carImages.setImageTwo(multipartFileTwo.getBytes());
        carImages.setImageThree(multipartFileThree.getBytes());

        updatedCar.setCarImages(carImages);

        return carService.saveCar(updatedCar);
    }

    @PutMapping(value = "/car")
    public APIResponse updateCar(
            @RequestParam(value = "imageOne", required = false) MultipartFile multipartFileOne,
            @RequestParam(value = "imageTwo", required = false) MultipartFile multipartFileTwo,
            @RequestParam(value = "imageThree", required = false) MultipartFile multipartFileThree,
            @RequestParam(value = "car") String car) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Car updatedCar = objectMapper.readValue(car, Car.class);

        CarImages carImages = new CarImages();

        if (multipartFileOne != null) {
            carImages.setImageOne(multipartFileOne.getBytes());
            updatedCar.setCarImages(carImages);
        }

        if (multipartFileTwo != null) {
            carImages.setImageTwo(multipartFileTwo.getBytes());
            updatedCar.setCarImages(carImages);
        }

        if (multipartFileThree != null) {
            carImages.setImageThree(multipartFileThree.getBytes());
            updatedCar.setCarImages(carImages);
        }

        return carService.updateCar(updatedCar);
    }

    @GetMapping(value = "/delete/{carId}")
    public APIResponse deleteCar(@PathVariable Integer carId) {
        return carService.deleteCar(carId);
    }

    @GetMapping(value = "/cars/{brandName}")
    public List<Car> getCarsByBrandName(@PathVariable String brandName) {
        return carService.getCarsByBrandName(brandName);
    }

    @GetMapping(value = "/cars/brand")
    public List<String> getAllCarBrands() {
        return carService.getAllCarBrandNames();
    }

    @GetMapping(value = "/cars/count")
    public Long countAllCars() {
        return carService.countAllCars();
    }

    @GetMapping(value = "/car/{carId}")
    public Optional<Car> getCarById(@PathVariable Integer carId) {
        return carService.findById(carId);
    }

    @GetMapping(value = "/car/available/{carId}")
    public APIResponse makeCarAvailable(@PathVariable Integer carId) {
        return carService.makeCarAvailable(carId);
    }

    @GetMapping(value = "/car/unavailable/{carId}")
    public APIResponse makeCarUnavailable(@PathVariable Integer carId) {
        return carService.makeCarUnavailable(carId);
    }
}

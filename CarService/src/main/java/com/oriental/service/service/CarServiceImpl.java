package com.oriental.service.service;

import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Car;
import com.oriental.service.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public Optional<Car> findById(Integer id) {
        return carRepository.findById(id);
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> getCarsByBrandName(String brandName) {
        if (brandName.equals("all"))
            return getAllCars();
        else
            return carRepository.findByCarBrandName(brandName);
    }

    @Override
    public APIResponse saveCar(Car car) {
        Car dbCar = carRepository.findByCarNumber(car.getCarNumber());

        if (dbCar == null) {
            car.setAvailability(Boolean.TRUE);
            car.setDateCreated(getCurrentDateTime());
            car.setStatus(Boolean.TRUE);
            Car createdCar = carRepository.save(car);

            if (createdCar != null)
                return new APIResponse(200, "Successful!");
            else
                return new APIResponse(404, "Unsuccessful!");
        } else {
            return new APIResponse(404, "Car Exists!");
        }
    }

    @Override
    public APIResponse updateCar(Car car) {
        Car currentCar = carRepository.findByCarNumber(car.getCarNumber());

        if (currentCar != null) {
            currentCar.setCarBrandName(car.getCarBrandName());
            currentCar.setCarModel(car.getCarModel());
            currentCar.setCarImages(car.getCarImages());
            currentCar.setFuelType(car.getFuelType());
            currentCar.setGearType(car.getGearType());
            currentCar.setNoOfPassengers(car.getNoOfPassengers());
            currentCar.setDoorsCount(car.getDoorsCount());
            currentCar.setMileagePerGallon(car.getMileagePerGallon());
            currentCar.setAcNonAc(car.getAcNonAc());
            currentCar.setBagsCanHold(car.getBagsCanHold());
            currentCar.setPricePerHour(car.getPricePerHour());

            // car.setAvailability(Boolean.TRUE);
            // car.setDateCreated(getCurrentDateTime());
            // car.setStatus(Boolean.TRUE);

            Car updatedCar = carRepository.save(currentCar);

            if (updatedCar != null) {
                return new APIResponse(200, "Successful!");
            } else {
                return new APIResponse(404, "Unsuccessful!");
            }
        } else {
            return new APIResponse(404, "No Car!");
        }
    }

    @Override
    public APIResponse deleteCar(Integer carId) {
        Optional<Car> currentCar = carRepository.findById(carId);
        if (currentCar.isPresent()) {
            Car car = currentCar.get();
            car.setStatus(Boolean.FALSE);

            carRepository.save(car);

            return new APIResponse(200, "Successful!");
        } else
            return new APIResponse(404, "No Car!");
    }

    @Override
    public APIResponse makeCarUnavailable(Integer carId) {
        Optional<Car> currentCar = findById(carId);

        if (currentCar.isPresent()) {
            Car car = currentCar.get();
            car.setAvailability(false);

            carRepository.save(car);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(404, "No Car!");
        }
    }

    @Override
    public APIResponse makeCarAvailable(Integer carId) {
        Optional<Car> currentCar = findById(carId);

        if (currentCar.isPresent()) {
            Car car = currentCar.get();
            car.setAvailability(true);

            carRepository.save(car);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(404, "No Car!");
        }
    }

    @Override
    public Long countAllCars() {
        return carRepository.count();
    }

    @Override
    public List<String> getAllCarBrandNames() {
        return carRepository.findAllCarBrandName();
    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(TimeZone.getTimeZone("Asia/Kolkata").toZoneId());
    }
}

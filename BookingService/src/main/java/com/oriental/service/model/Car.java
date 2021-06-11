package com.oriental.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Car {

    private Integer id;
    private String carBrandName;
    private String carNumber;

    private CarModel carModel;

    private CarImages carImages;

    private String fuelType;
    private String gearType;
    private String noOfPassengers;
    private Integer doorsCount;
    private Integer mileagePerGallon;
    private Boolean acNonAc;
    private Integer bagsCanHold;
    private BigDecimal pricePerHour;
    private Boolean availability;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;

    private Boolean status;
}

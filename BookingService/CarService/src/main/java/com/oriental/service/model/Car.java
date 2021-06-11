package com.oriental.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "car")
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String carBrandName;
    private String carNumber;

    @OneToOne(cascade = CascadeType.ALL)
    private CarModel carModel;

    @OneToOne(cascade = CascadeType.ALL)
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

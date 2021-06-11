package com.oriental.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Booking {

    private Integer id;
    private Integer carId;
    private Integer customerId;
    private String pickupLocation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateBooked;
    private String status; // b - booked, c - cancelled, x - done

    private Payment payment;

    private Car car;
}

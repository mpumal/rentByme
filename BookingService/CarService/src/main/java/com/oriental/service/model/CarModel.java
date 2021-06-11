package com.oriental.service.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "car_model")
@Data
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String modelName;
    private String modelColor;
}

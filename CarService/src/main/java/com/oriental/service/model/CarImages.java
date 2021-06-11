package com.oriental.service.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "car_images")
@Data
public class CarImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private byte[] imageOne;
    @Lob
    private byte[] imageTwo;
    @Lob
    private byte[] imageThree;
}

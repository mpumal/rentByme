package com.oriental.service.model;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
}

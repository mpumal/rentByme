package com.oriental.service.controller;

import com.oriental.service.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/userservice")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping(value = "/user")
    public UserDetails getUser() {
        System.err.println("Calling...");
        return userDetailsService.loadUserByUsername("manoj");
    }
}

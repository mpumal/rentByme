package com.oriental.service.controller;

import com.oriental.service.exceptionhandler.ResourceNotFoundException;
import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Booking;
import com.oriental.service.model.User;
import com.oriental.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/user/id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) throws ResourceNotFoundException {
        return userService.findUserById(userId);
    }

    @PostMapping(value = "/user")
    public APIResponse createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping(value = "/admin")
    public APIResponse createAdminUser(@RequestBody User user) {
        return userService.createAdminUser(user);
    }

    @PutMapping(value = "/user/{username}")
    public APIResponse updateUser(@PathVariable String username, @RequestBody User user) {
        return userService.updateUser(username, user);
    }

    @GetMapping(value = "/user/{username}")
    public APIResponse deleteUser(@PathVariable String username) throws ResourceNotFoundException {
        return userService.deleteUser(username);
    }

    @GetMapping(value = "/login/{username}/{password}")
    public APIResponse userLogin(@PathVariable String username, @PathVariable String password) throws ResourceNotFoundException {
        return userService.userLogin(username, password);
    }

    @GetMapping(value = "/refreshtoken/{username}/{refreshToken}")
    public APIResponse refreshAccessToken(@PathVariable String username, @PathVariable String refreshToken) {
        return userService.refreshAccessToken(username, refreshToken);
    }

    @GetMapping(value = "/checktoken/{username}/{accessToken}/{refreshToken}")
    public APIResponse checkAccessToken(@PathVariable String username, @PathVariable String accessToken, @PathVariable String refreshToken) {
        return userService.checkAccessToken(username, accessToken, refreshToken);
    }

    @GetMapping(value = "/get/booking/{username}")
    public List<Booking> getUserBookingDetails(@PathVariable String username, @RequestHeader("Authorization") String authorization) {
        return userService.getUserBookingDetails(username, authorization);
    }

    @GetMapping(value = "/get/customer/{username}")
    public APIResponse getCustomerID(@PathVariable String username) {
        return userService.getCustomerID(username);
    }

    @GetMapping(value = "/count")
    public Long countAllUsers() {
        return userService.countAllUsers();
    }

    @GetMapping(value = "/forgot/{emailAddress}")
    public APIResponse forgotPassword(@PathVariable String emailAddress) {
        return userService.forgotPassword(emailAddress);
    }
}

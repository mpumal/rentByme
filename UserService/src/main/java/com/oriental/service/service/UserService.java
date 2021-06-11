package com.oriental.service.service;

import com.oriental.service.exceptionhandler.ResourceNotFoundException;
import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Booking;
import com.oriental.service.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<Booking> getUserBookingDetails(String username, String authorization);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    ResponseEntity<User> findUserById(Integer userId) throws ResourceNotFoundException;

    APIResponse getCustomerID(String username);

    APIResponse createUser(User user);

    APIResponse createAdminUser(User user);

    APIResponse updateUser(String username, User user);

    APIResponse deleteUser(String username) throws ResourceNotFoundException;

    APIResponse userLogin(String username, String password) throws ResourceNotFoundException;

    APIResponse refreshAccessToken(String username, String refreshToken);

    APIResponse checkAccessToken(String username, String accessToken, String refreshToken);

    APIResponse forgotPassword(String emailAddress);

    Long countAllUsers();
}

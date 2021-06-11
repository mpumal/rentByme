package com.oriental.service.controller;

import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Booking;
import com.oriental.service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping(value = "/book/{username}/{status}/{payedAmount}/{totalAmount}")
    public APIResponse createBooking(@PathVariable String username, @PathVariable String status, @PathVariable BigDecimal payedAmount, @PathVariable BigDecimal totalAmount, @RequestBody Booking booking, @RequestHeader("Authorization") String authorization) {
        return bookingService.createBooking(username, status, payedAmount, totalAmount, booking, authorization);
    }

    @PutMapping(value = "/book/{bookingId}")
    public APIResponse updateBooking(@PathVariable Integer bookingId, @RequestBody Booking booking) {
        return bookingService.updateBooking(bookingId, booking);
    }

    @DeleteMapping(value = "/book/{bookingId}")
    public APIResponse deleteBooking(@PathVariable Integer bookingId, @RequestHeader("Authorization") String authorization) {
        return bookingService.deleteBooking(bookingId, authorization);
    }

    @GetMapping(value = "/get/{bookingId}")
    public Booking getBookingDetails(@PathVariable Integer bookingId, @RequestHeader("Authorization") String authorization) {
        return bookingService.getBookingDetails(bookingId, authorization);
    }

    @GetMapping(value = "/get/customer/{customerId}")
    public List<Booking> getBookingDetailsByCustomer(@PathVariable Integer customerId, @RequestHeader("Authorization") String authorization) {
        return bookingService.getBookingDetailsByCustomer(customerId, authorization);
    }

    @GetMapping(value = "/get/bookings")
    public List<Booking> getAllBookingDetails(@RequestHeader("Authorization") String authorization) {
        return bookingService.getAllBookingDetails(authorization);
    }

    @GetMapping(value = "/complete/{bookingId}/{status}")
    public APIResponse completeBooking(@PathVariable Integer bookingId, @PathVariable String status, @RequestHeader("Authorization") String authorization) {
        return bookingService.completeBooking(bookingId, status, authorization);
    }

    @GetMapping(value = "/cancel/{bookingId}")
    public APIResponse cancelBooking(@PathVariable Integer bookingId, @RequestHeader("Authorization") String authorization) {
        return bookingService.cancelBooking(bookingId, authorization);
    }

    @GetMapping(value = "/count")
    public Long countAllBookings() {
        return bookingService.countAllBookings();
    }
}

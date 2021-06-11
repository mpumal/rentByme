package com.oriental.service.service;

import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Booking;

import java.math.BigDecimal;
import java.util.List;

public interface BookingService {

    APIResponse createBooking(String username, String status, BigDecimal payedAmount, BigDecimal totalAmount, Booking booking, String authorization);

    APIResponse updateBooking(Integer bookingId, Booking booking);

    APIResponse deleteBooking(Integer bookingId, String authorization);

    APIResponse completeBooking(Integer bookingId, String status, String authorization);

    APIResponse cancelBooking(Integer bookingId, String authorization);

    Long countAllBookings();

    Booking getBookingDetails(Integer bookingId, String authorization);

    List<Booking> getAllBookingDetails(String authorization);

    List<Booking> getBookingDetailsByCustomer(Integer customerId, String authorization);
}

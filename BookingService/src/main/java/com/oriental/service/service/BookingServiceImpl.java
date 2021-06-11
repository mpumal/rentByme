package com.oriental.service.service;

import com.oriental.service.appconfig.URLConfiguration;
import com.oriental.service.exceptionhandler.RestTemplateResponseErrorHandler;
import com.oriental.service.model.*;
import com.oriental.service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public APIResponse createBooking(String username, String status, BigDecimal payedAmount, BigDecimal totalAmount, Booking booking, String authorization) {
        // get customer id from user service related to username
        HttpEntity httpEntityU = new HttpEntity<>(setAuthorizationHeader(authorization));

        restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

        ResponseEntity<APIResponse> responseEntityU = restTemplate.exchange(
                URLConfiguration.GET_CUSTOMER_ID + username, HttpMethod.GET, httpEntityU, APIResponse.class);

        if (responseEntityU.getStatusCode() == HttpStatus.OK) {
            APIResponse apiResponse = responseEntityU.getBody();

            if (!apiResponse.getMessage().equals("Unsuccessful!")) {
                booking.setCustomerId(Integer.valueOf(apiResponse.getParameter()));
                booking.setDateBooked(getCurrentDateTime());
                booking.setStatus("B");

                Booking createdBooking = bookingRepository.save(booking);

                if (createdBooking != null) {
                    Payment payment = new Payment();
                    payment.setBookingId(createdBooking.getId());
                    payment.setCustomerId(createdBooking.getCustomerId());
                    payment.setPayedAmount(payedAmount);
                    payment.setTotalRentPrice(totalAmount);
                    payment.setStatus(status);

                    // send data to payment service
                    HttpEntity httpEntityP = new HttpEntity<>(payment, setAuthorizationHeader(authorization));
                    // send data to car service
                    HttpEntity httpEntityC = new HttpEntity<>(setAuthorizationHeader(authorization));

                    restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

                    ResponseEntity<APIResponse> responseEntityP = restTemplate.exchange(
                            URLConfiguration.ADD_PAYMENT, HttpMethod.POST, httpEntityP, APIResponse.class);

                    ResponseEntity<APIResponse> responseEntityC = restTemplate.exchange(
                            URLConfiguration.MAKE_CAR_UNAVAILABLE + booking.getCarId(), HttpMethod.GET, httpEntityC, APIResponse.class);

                    if (responseEntityP.getStatusCode() == HttpStatus.OK
                            && responseEntityC.getStatusCode() == HttpStatus.OK)
                        return new APIResponse(200, "Successful!");
                    else
                        return new APIResponse(400, "Unsuccessful!");
                } else {
                    return new APIResponse(400, "Unsuccessful!");
                }
            } else {
                return new APIResponse(400, "Unsuccessful!");
            }
        } else {
            return new APIResponse(400, "Unsuccessful!");
        }
    }

    @Override
    public APIResponse updateBooking(Integer bookingId, Booking booking) {
        Optional<Booking> currentBooking = bookingRepository.findById(bookingId);

        if (currentBooking.isPresent()) {
            bookingRepository.save(booking);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(400, "Booking Not Found!");
        }
    }

    @Override
    public APIResponse deleteBooking(Integer bookingId, String authorization) {
        if (bookingRepository.existsById(bookingId)) {
            bookingRepository.deleteById(bookingId);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(400, "Booking Not Found!");
        }
    }

    @Override
    public APIResponse completeBooking(Integer bookingId, String status, String authorization) {
        Optional<Booking> currentBooking = bookingRepository.findById(bookingId);

        if (currentBooking.isPresent()) {
            Booking updateBooking = currentBooking.get();
            updateBooking.setStatus(status);

            bookingRepository.save(updateBooking);

            // send data to car service
            HttpEntity httpEntity = new HttpEntity<>(setAuthorizationHeader(authorization));
            ResponseEntity<APIResponse> responseEntity = restTemplate.exchange(
                    URLConfiguration.MAKE_CAR_AVAILABLE + updateBooking.getCarId(), HttpMethod.GET,
                    httpEntity, APIResponse.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK)
                return new APIResponse(200, "Successful!");
            else
                return new APIResponse(400, "Unsuccessful!");
        } else {
            return new APIResponse(400, "Booking Not Found!");
        }
    }

    @Override
    public APIResponse cancelBooking(Integer bookingId, String authorization) {
        Optional<Booking> currentBooking = bookingRepository.findById(bookingId);

        if (currentBooking.isPresent()) {
            Booking booking = currentBooking.get();
            booking.setStatus("C");

            bookingRepository.save(booking);

            // send data to car service
            HttpEntity httpEntity = new HttpEntity<>(setAuthorizationHeader(authorization));
            ResponseEntity<APIResponse> responseEntity = restTemplate.exchange(
                    URLConfiguration.MAKE_CAR_AVAILABLE + booking.getCarId(), HttpMethod.GET,
                    httpEntity, APIResponse.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK)
                return new APIResponse(200, "Successful!");
            else
                return new APIResponse(400, "Unsuccessful!");
        } else {
            return new APIResponse(400, "Booking Not Found!");
        }
    }

    @Override
    public Long countAllBookings() {
        return bookingRepository.count();
    }

    @Override
    public Booking getBookingDetails(Integer bookingId, String authorization) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (booking.isPresent()) {
            Booking currentBookingData = booking.get();

            // get payment data related to booking id
            HttpEntity httpEntity = new HttpEntity<>(setAuthorizationHeader(authorization));

            restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

            ResponseEntity<Payment> responseEntity = restTemplate.exchange(
                    URLConfiguration.GET_PAYMENT_BY_BOOKING_ID + bookingId, HttpMethod.GET, httpEntity, Payment.class);

            currentBookingData.setPayment(responseEntity.getBody());

            return currentBookingData;
        } else {
            return null;
        }
    }

    @Override
    public List<Booking> getAllBookingDetails(String authorization) {
        List<Booking> allBookings = bookingRepository.findAllByOrderByIdDesc();

        if (!allBookings.isEmpty()) {
            HttpEntity httpEntity = new HttpEntity<>(setAuthorizationHeader(authorization));

            restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

            ResponseEntity<Car[]> carEntity = restTemplate.exchange(
                    URLConfiguration.GET_ALL_CAR_DETAILS, HttpMethod.GET, httpEntity, Car[].class);

            if (carEntity.getStatusCode() == HttpStatus.OK) {
                List<Car> carList = Arrays.asList(carEntity.getBody());

                ResponseEntity<User[]> userEntity = restTemplate.exchange(
                        URLConfiguration.GET_ALL_USER_DETAILS, HttpMethod.GET, httpEntity, User[].class);

                List<User> userList = Arrays.asList(userEntity.getBody());

                for (Booking booking : allBookings) {
                    for (Car car : carList) {
                        if (booking.getCarId().equals(car.getId())) {
                            booking.setCar(car);
                        }
                    }

                    for (User user : userList) {
                        if (booking.getCustomerId().equals(user.getId())) {
                            booking.setUser(user);
                        }
                    }
                }

                return allBookings;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<Booking> getBookingDetailsByCustomer(Integer customerId, String authorization) {
        List<Booking> bookingList = bookingRepository.findByCustomerIdOrderByIdDesc(customerId);

        if (!bookingList.isEmpty()) {
            // get list of payment data related to customer
            HttpEntity httpEntity = new HttpEntity<>(setAuthorizationHeader(authorization));

            restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

            ResponseEntity<Payment[]> paymentEntity = restTemplate.exchange(
                    URLConfiguration.GET_PAYMENT_BY_CUSTOMER_ID + customerId, HttpMethod.GET, httpEntity, Payment[].class);

            if (paymentEntity.getStatusCode() == HttpStatus.OK) {
                List<Payment> paymentList = Arrays.asList(paymentEntity.getBody());

                ResponseEntity<Car[]> carEntity = restTemplate.exchange(
                        URLConfiguration.GET_ALL_CAR_DETAILS, HttpMethod.GET, httpEntity, Car[].class);

                List<Car> carList = Arrays.asList(carEntity.getBody());

                for (Booking booking : bookingList) {
                    for (Payment payment : paymentList) {
                        if (booking.getId().equals(payment.getBookingId())) {
                            booking.setPayment(payment);
                        }
                    }

                    for (Car car : carList) {
                        if (booking.getCarId().equals(car.getId())) {
                            booking.setCar(car);
                        }
                    }
                }

                return bookingList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private HttpHeaders setAuthorizationHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, accessToken);
        // httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return httpHeaders;
    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(TimeZone.getTimeZone("Asia/Kolkata").toZoneId());
    }
}

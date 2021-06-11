package com.oriental.service.service;

import com.oriental.service.appconfig.URLConfiguration;
import com.oriental.service.exceptionhandler.RestTemplateResponseErrorHandler;
import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Payment;
import com.oriental.service.model.User;
import com.oriental.service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public APIResponse createPayment(Payment payment) {
        payment.setPaymentDate(getCurrentDateTime());

        Payment createdPayment = paymentRepository.save(payment);

        if (createdPayment != null)
            return new APIResponse(200, "Successful!");
        else
            return new APIResponse(400, "Unsuccessful!");
    }

    @Override
    public APIResponse updatePayment(Integer bookingId, Payment payment) {
        Payment currentPayment = paymentRepository.findByBookingId(bookingId);

        if (currentPayment != null) {
            if (payment.getStatus().equals("R")) {
                payment.setPayedAmount(new BigDecimal(0));
                payment.setRefundRequest(0);

                paymentRepository.save(payment);

                return new APIResponse(200, "Successful!");
            } else {
                paymentRepository.save(payment);

                return new APIResponse(200, "Successful!");
            }
        } else {
            return new APIResponse(400, "Unsuccessful!");
        }
    }

    @Override
    public APIResponse createRefundRequest(Integer bookingId) {
        Payment currentPayment = paymentRepository.findByBookingId(bookingId);

        if (currentPayment != null) {
            currentPayment.setRefundRequest(1);

            paymentRepository.save(currentPayment);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(400, "Unsuccessful!");
        }
    }

    @Override
    public APIResponse approveRefund(Integer paymentId) {
        Optional<Payment> currentPayment = paymentRepository.findById(paymentId);

        if (currentPayment.isPresent()) {
            Payment updatePayment = currentPayment.get();

            updatePayment.setStatus("R");
            updatePayment.setRefundRequest(0);

            paymentRepository.save(updatePayment);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(400, "Unsuccessful!");
        }
    }

    @Override
    public APIResponse isRefundPending(Integer bookingId) {
        Payment currentPayment = paymentRepository.findByBookingId(bookingId);

        if (currentPayment.getRefundRequest() == null || currentPayment.getRefundRequest().equals(0)) {
            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(400, "Unsuccessful!");
        }
    }

    @Override
    public Payment getPaymentDetails(Integer bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    @Override
    public Long countRefundRequests() {
        return paymentRepository.countRefundRequests();
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return paymentRepository.getTotalRevenue();
    }

    @Override
    public List<Payment> getPaymentDetailsByCustomer(Integer customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Payment> getRefundRequests(Integer refundRequest, String authorization) {
        List<Payment> paymentList = paymentRepository.findByRefundRequest(refundRequest);

        return getPaymentWithUsers(paymentList, authorization);
    }

    @Override
    public List<Payment> getAllPaymentDetails(String authorization) {
        List<Payment> paymentList = paymentRepository.findAllByOrderByIdDesc();

        return getPaymentWithUsers(paymentList, authorization);
    }

    private List<Payment> getPaymentWithUsers(List<Payment> paymentList, String authorization) {
        if (!paymentList.isEmpty()) {
            HttpEntity httpEntity = new HttpEntity<>(setAuthorizationHeader(authorization));

            restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

            ResponseEntity<User[]> userEntity = restTemplate.exchange(
                    URLConfiguration.GET_ALL_USER_DETAILS, HttpMethod.GET, httpEntity, User[].class);

            List<User> userList = Arrays.asList(userEntity.getBody());

            for (Payment payment : paymentList) {
                for (User user : userList) {
                    if (payment.getCustomerId().equals(user.getId())) {
                        payment.setUser(user);
                    }
                }
            }

            return paymentList;
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

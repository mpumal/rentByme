package com.oriental.service.controller;

import com.oriental.service.model.APIResponse;
import com.oriental.service.model.Payment;
import com.oriental.service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/pay")
    public APIResponse createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @PutMapping(value = "/pay/{bookingId}")
    public APIResponse updatePayment(@PathVariable Integer bookingId, @RequestBody Payment payment) {
        return paymentService.updatePayment(bookingId, payment);
    }

    @GetMapping(value = "/refund/{bookingId}")
    public APIResponse createRefundRequest(@PathVariable Integer bookingId) {
        return paymentService.createRefundRequest(bookingId);
    }

    @GetMapping(value = "/get/{bookingId}")
    public Payment getPaymentDetails(@PathVariable Integer bookingId) {
        return paymentService.getPaymentDetails(bookingId);
    }

    @GetMapping(value = "/get/customer/{customerId}")
    public List<Payment> getPaymentDetailsByCustomer(@PathVariable Integer customerId) {
        return paymentService.getPaymentDetailsByCustomer(customerId);
    }

    @GetMapping(value = "/get/refund/{refundRequest}")
    public List<Payment> getRefundRequests(@PathVariable Integer refundRequest, @RequestHeader("Authorization") String authorization) {
        return paymentService.getRefundRequests(refundRequest, authorization);
    }

    @GetMapping(value = "/get")
    public List<Payment> getAllPayments(@RequestHeader("Authorization") String authorization) {
        return paymentService.getAllPaymentDetails(authorization);
    }

    @GetMapping(value = "/approve/{paymentId}")
    public APIResponse approveRefund(@PathVariable Integer paymentId) {
        return paymentService.approveRefund(paymentId);
    }

    @GetMapping(value = "/count/refund")
    public Long countRefundRequests() {
        return paymentService.countRefundRequests();
    }

    @GetMapping(value = "/revenue")
    public BigDecimal getTotalRevenue() {
        return paymentService.getTotalRevenue();
    }

    @GetMapping(value = "/get/pending/{bookingId}")
    public APIResponse isRefundPending(@PathVariable Integer bookingId) {
        return paymentService.isRefundPending(bookingId);
    }
}

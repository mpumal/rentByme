package com.oriental.service.repository;

import com.oriental.service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Payment findByBookingId(Integer bookingId);

    List<Payment> findByCustomerId(Integer customerId);

    List<Payment> findByRefundRequest(Integer refundRequest);

    List<Payment> findAllByOrderByIdDesc();

    @Query("SELECT COUNT(refundRequest) FROM Payment WHERE refundRequest = 1")
    Long countRefundRequests();

    @Query("SELECT SUM(payedAmount) FROM Payment WHERE status <> 'R'")
    BigDecimal getTotalRevenue();
}

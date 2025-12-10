package com.vfsglobal.payments.repository;

import com.vfsglobal.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByPaymentId(UUID paymentId);
    
    Optional<Payment> findByPaymentReference(String paymentReference);
    
    Optional<Payment> findByApplicationId(UUID applicationId);
}


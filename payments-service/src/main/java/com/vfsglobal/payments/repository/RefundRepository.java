package com.vfsglobal.payments.repository;

import com.vfsglobal.payments.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefundRepository extends JpaRepository<Refund, UUID> {
    Optional<Refund> findByRefundId(UUID refundId);
    
    Optional<Refund> findByRefundReference(String refundReference);
    
    List<Refund> findByPaymentId(UUID paymentId);
    
    List<Refund> findByApplicationId(UUID applicationId);
}


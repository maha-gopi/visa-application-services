package com.vfsglobal.payments.service;

import com.vfsglobal.payments.dto.*;
import com.vfsglobal.payments.entity.Payment;
import com.vfsglobal.payments.entity.Refund;
import com.vfsglobal.payments.repository.PaymentRepository;
import com.vfsglobal.payments.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setPaymentReference(generatePaymentReference());
        // TODO: Translate logical ID (reference_number) to UUID
        // This should call bookings-service or lookup Application entity if available
        payment.setApplicationId(UUID.randomUUID()); // TODO: Translate bookingReferenceNumber to UUID
        payment.setTotalAmount(request.getAmount());
        payment.setSubTotal(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMode(mapPaymentMethod(request.getPaymentMethod()));
        payment.setStatus(Payment.PaymentStatus.PROCESSING);
        payment.setCreatedDate(OffsetDateTime.now());
        payment.setUpdatedDate(OffsetDateTime.now());
        
        // Simulate payment processing
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setPaymentDate(OffsetDateTime.now());
        payment.setGatewayTransactionId("txn-" + UUID.randomUUID().toString().substring(0, 8));
        
        if (request.getCardDetails() != null) {
            String cardNumber = request.getCardDetails().getCardNumber().replaceAll("\\s", "");
            payment.setCardLastFour(cardNumber.substring(cardNumber.length() - 4));
            payment.setCardBrand(detectCardBrand(cardNumber));
        }
        
        payment = paymentRepository.save(payment);
        
        return PaymentResponse.builder()
            .paymentId(payment.getPaymentReference()) // Expose logical ID instead of UUID
            .bookingId(request.getBookingReferenceNumber()) // Expose logical ID instead of UUID
            .status(payment.getStatus().name().toLowerCase())
            .amount(payment.getTotalAmount())
            .currency(payment.getCurrency())
            .paymentMethod(request.getPaymentMethod())
            .transactionId(payment.getGatewayTransactionId())
            .processedAt(payment.getPaymentDate())
            .build();
    }
    
    public PaymentDetailResponse getPaymentById(String paymentReference) {
        // Accept logical ID (payment_reference) instead of UUID
        Payment payment = paymentRepository.findByPaymentReference(paymentReference)
            .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentReference));
        return mapToDetailResponse(payment);
    }
    
    public PaymentDetailResponse getPaymentByBookingId(String bookingReferenceNumber) {
        // TODO: Translate logical ID (reference_number) to UUID
        // This should call bookings-service or lookup Application entity if available
        UUID applicationId = UUID.randomUUID(); // TODO: Translate bookingReferenceNumber to UUID
        Payment payment = paymentRepository.findByApplicationId(applicationId)
            .orElseThrow(() -> new RuntimeException("Payment not found for booking: " + bookingReferenceNumber));
        return mapToDetailResponse(payment);
    }
    
    @Transactional
    public RefundResponse processRefund(String paymentReference, RefundRequest request) {
        // Accept logical ID (payment_reference) instead of UUID
        Payment payment = paymentRepository.findByPaymentReference(paymentReference)
            .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentReference));
        
        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }
        
        Refund refund = new Refund();
        refund.setRefundId(UUID.randomUUID());
        refund.setRefundReference(generateRefundReference());
        refund.setPaymentId(payment.getPaymentId());
        refund.setApplicationId(payment.getApplicationId());
        refund.setRefundAmount(request.getAmount() != null ? request.getAmount() : payment.getTotalAmount());
        refund.setCurrency(payment.getCurrency());
        refund.setReason(request.getReason());
        refund.setRefundType(request.getAmount() != null && request.getAmount().compareTo(payment.getTotalAmount()) < 0 ? 
            "PARTIAL" : "FULL");
        refund.setRefundMethod("ORIGINAL");
        refund.setStatus(Refund.RefundStatus.PROCESSING);
        refund.setRequestedByUserType("APPLICANT");
        refund.setRequestedByUserId(UUID.randomUUID()); // Should come from auth context
        refund.setRequestedDate(OffsetDateTime.now());
        
        refund = refundRepository.save(refund);
        
        // Update payment status
        if (refund.getRefundType().equals("FULL")) {
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
        } else {
            payment.setStatus(Payment.PaymentStatus.PARTIALLY_REFUNDED);
        }
        paymentRepository.save(payment);
        
        return RefundResponse.builder()
            .refundId(refund.getRefundReference()) // Expose logical ID instead of UUID
            .paymentId(payment.getPaymentReference()) // Expose logical ID instead of UUID
            .status(refund.getStatus().name().toLowerCase())
            .amount(refund.getRefundAmount())
            .currency(refund.getCurrency())
            .reason(refund.getReason())
            .processedAt(OffsetDateTime.now())
            .estimatedCompletion(OffsetDateTime.now().plusDays(7))
            .build();
    }
    
    private String generatePaymentReference() {
        return "PAY-" + OffsetDateTime.now().getYear() + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    private String generateRefundReference() {
        return "REF-" + OffsetDateTime.now().getYear() + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    private Payment.PaymentMode mapPaymentMethod(String method) {
        return switch (method.toLowerCase()) {
            case "card" -> Payment.PaymentMode.CREDIT_CARD;
            case "bank_transfer" -> Payment.PaymentMode.BANK_TRANSFER;
            case "cash" -> Payment.PaymentMode.CASH;
            default -> Payment.PaymentMode.ONLINE;
        };
    }
    
    private String detectCardBrand(String cardNumber) {
        if (cardNumber.startsWith("4")) return "visa";
        if (cardNumber.startsWith("5")) return "mastercard";
        if (cardNumber.startsWith("3")) return "amex";
        return "other";
    }
    
    private PaymentDetailResponse mapToDetailResponse(Payment payment) {
        return PaymentDetailResponse.builder()
            .id(payment.getPaymentReference()) // Expose logical ID instead of UUID
            .bookingId(payment.getApplicationId().toString()) // Keep UUID for now, will update when bookings-service integration is done
            .status(payment.getStatus().name().toLowerCase())
            .amount(payment.getTotalAmount())
            .currency(payment.getCurrency())
            .paymentMethod(payment.getPaymentMode().name().toLowerCase())
            .transactionId(payment.getGatewayTransactionId())
            .cardLast4(payment.getCardLastFour())
            .cardBrand(payment.getCardBrand())
            .processedAt(payment.getPaymentDate())
            .createdAt(payment.getCreatedDate())
            .updatedAt(payment.getUpdatedDate())
            .build();
    }
}


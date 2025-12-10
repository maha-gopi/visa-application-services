package com.vfsglobal.payments.controller;

import com.vfsglobal.payments.dto.*;
import com.vfsglobal.payments.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payments", description = "API endpoints for processing payments and managing payment transactions")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/process")
    @Operation(summary = "Process payment", description = "Processes a payment for a booking")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        log.debug("Processing payment for booking {}", request.getBookingReferenceNumber());
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{paymentReference}")
    @Operation(summary = "Get payment details", description = "Retrieves detailed information about a specific payment using payment reference")
    public ResponseEntity<PaymentDetailResponse> getPaymentByReference(
            @io.swagger.v3.oas.annotations.Parameter(description = "Payment reference number", example = "PAY-2024-123456")
            @PathVariable String paymentReference) {
        log.debug("Getting payment by reference {}", paymentReference);
        PaymentDetailResponse response = paymentService.getPaymentById(paymentReference);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/booking/{bookingReferenceNumber}")
    @Operation(summary = "Get payment by booking reference", description = "Retrieves payment information for a specific booking using reference number")
    public ResponseEntity<PaymentDetailResponse> getPaymentByBookingReference(
            @io.swagger.v3.oas.annotations.Parameter(description = "Booking reference number", example = "APP20241208000001")
            @PathVariable String bookingReferenceNumber) {
        log.debug("Getting payment by booking reference {}", bookingReferenceNumber);
        PaymentDetailResponse response = paymentService.getPaymentByBookingId(bookingReferenceNumber);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{paymentReference}/refund")
    @Operation(summary = "Process refund", description = "Initiates a refund for a completed payment using payment reference")
    public ResponseEntity<RefundResponse> processRefund(
            @io.swagger.v3.oas.annotations.Parameter(description = "Payment reference number", example = "PAY-2024-123456")
            @PathVariable String paymentReference,
            @RequestBody RefundRequest request) {
        log.debug("Processing refund for payment {}", paymentReference);
        RefundResponse response = paymentService.processRefund(paymentReference, request);
        return ResponseEntity.ok(response);
    }
}


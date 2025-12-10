package com.vfsglobal.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String bookingReferenceNumber; // Logical ID: application reference_number (e.g., APP20241208000001)
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private CardDetails cardDetails;
    private String returnUrl;
}


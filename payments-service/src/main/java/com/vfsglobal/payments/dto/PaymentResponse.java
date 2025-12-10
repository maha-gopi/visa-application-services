package com.vfsglobal.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String bookingId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String transactionId;
    private OffsetDateTime processedAt;
    private String redirectUrl;
}


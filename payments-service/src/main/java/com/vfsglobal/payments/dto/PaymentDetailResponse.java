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
public class PaymentDetailResponse {
    private String id;
    private String bookingId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String transactionId;
    private String cardLast4;
    private String cardBrand;
    private OffsetDateTime processedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String failureReason;
}


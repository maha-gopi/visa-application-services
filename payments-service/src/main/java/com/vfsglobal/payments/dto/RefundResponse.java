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
public class RefundResponse {
    private String refundId;
    private String paymentId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String reason;
    private OffsetDateTime processedAt;
    private OffsetDateTime estimatedCompletion;
}


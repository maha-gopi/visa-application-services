package com.vfsglobal.bookings.dto;

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
public class BookingSummary {
    private String id;
    private String referenceNumber;
    private String status;
    private String visaType;
    private String appointmentDate;
    private BigDecimal totalAmount;
    private String currency;
    private OffsetDateTime createdAt;
}


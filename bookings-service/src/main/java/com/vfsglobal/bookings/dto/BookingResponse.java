package com.vfsglobal.bookings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String id;
    private String referenceNumber;
    private String status;
    private VisaTypeInfo visaType;
    private PersonalDetails personalDetails;
    private String photo;
    private String appointmentDate;
    private String appointmentTime;
    private List<SelectedService> selectedServices;
    private BigDecimal servicesTotal;
    private BigDecimal visaFee;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private String locationId;
    private String locationName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}


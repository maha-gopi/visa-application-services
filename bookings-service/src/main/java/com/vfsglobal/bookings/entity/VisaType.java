package com.vfsglobal.bookings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "visa_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisaType {
    @Id
    @Column(name = "visa_type_id")
    private UUID visaTypeId;

    @Column(name = "visa_type_code", nullable = false, length = 20, unique = true)
    private String visaTypeCode;

    @Column(name = "visa_type_name", nullable = false, length = 100)
    private String visaTypeName;

    @Column(name = "base_fee", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal baseFee = java.math.BigDecimal.ZERO;

    @Column(name = "country_id", nullable = false)
    private UUID countryId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}


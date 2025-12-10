package com.vfsglobal.visatypes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "processing_time_days")
    private Integer processingTimeDays;

    @Column(name = "validity_days")
    private Integer validityDays;

    @Column(name = "max_stay_days")
    private Integer maxStayDays;

    @Column(name = "entry_type", length = 20)
    private String entryType;

    @Column(name = "base_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFee = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @Column(name = "requires_biometric", nullable = false)
    private Boolean requiresBiometric = true;

    @Column(name = "requires_interview", nullable = false)
    private Boolean requiresInterview = false;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 999;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private OffsetDateTime updatedDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}



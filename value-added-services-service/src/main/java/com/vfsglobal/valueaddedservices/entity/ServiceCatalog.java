package com.vfsglobal.valueaddedservices.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_catalog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCatalog {

    @Id
    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "service_code", nullable = false, length = 20, unique = true)
    private String serviceCode;

    @Column(name = "service_name", nullable = false, length = 255)
    private String serviceName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "service_type", nullable = false, length = 50)
    private String serviceType;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "is_location_specific", nullable = false)
    private Boolean isLocationSpecific = false;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 999;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private OffsetDateTime updatedDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}



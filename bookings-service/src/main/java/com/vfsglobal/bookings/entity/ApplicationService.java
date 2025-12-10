package com.vfsglobal.bookings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationService {
    @Id
    @Column(name = "application_service_id")
    private UUID applicationServiceId;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "added_date", nullable = false)
    private OffsetDateTime addedDate;

    @Column(name = "completed_date")
    private OffsetDateTime completedDate;
}


package com.vfsglobal.payments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "refund")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    @Id
    @Column(name = "refund_id")
    private UUID refundId;

    @Column(name = "refund_reference", nullable = false, length = 50, unique = true)
    private String refundReference;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "refund_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "refund_type", nullable = false, length = 20)
    private String refundType;

    @Column(name = "refund_method", nullable = false, length = 30)
    private String refundMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "refund_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private RefundStatus status = RefundStatus.PENDING;

    @Column(name = "requested_by_user_type", nullable = false, length = 20)
    private String requestedByUserType;

    @Column(name = "requested_by_user_id", nullable = false)
    private UUID requestedByUserId;

    @Column(name = "requested_date", nullable = false)
    private OffsetDateTime requestedDate;

    @Column(name = "approved_by_staff_id")
    private UUID approvedByStaffId;

    @Column(name = "approved_date")
    private OffsetDateTime approvedDate;

    @Column(name = "processed_by_staff_id")
    private UUID processedByStaffId;

    @Column(name = "processed_date")
    private OffsetDateTime processedDate;

    @Column(name = "gateway_refund_id", length = 100)
    private String gatewayRefundId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum RefundStatus {
        PENDING, APPROVED, REJECTED, PROCESSING, COMPLETED, FAILED
    }
}


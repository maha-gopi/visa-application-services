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
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "payment_reference", nullable = false, length = 50, unique = true)
    private String paymentReference;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "vac_id")
    private UUID vacId;

    @Column(name = "sub_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false, columnDefinition = "payment_mode")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PaymentMode paymentMode;

    @Column(name = "payment_gateway", length = 50)
    private String paymentGateway;

    @Column(name = "gateway_transaction_id", length = 100)
    private String gatewayTransactionId;

    @Column(name = "gateway_response", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String gatewayResponse;

    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;

    @Column(name = "card_brand", length = 20)
    private String cardBrand;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "payment_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "receipt_number", length = 50, unique = true)
    private String receiptNumber;

    @Column(name = "receipt_path", length = 500)
    private String receiptPath;

    @Column(name = "collected_by_staff_id")
    private UUID collectedByStaffId;

    @Column(name = "payment_date")
    private OffsetDateTime paymentDate;

    @Column(name = "reconciliation_status", nullable = false, length = 20)
    private String reconciliationStatus = "PENDING";

    @Column(name = "reconciliation_date")
    private OffsetDateTime reconciliationDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private OffsetDateTime updatedDate;

    public enum PaymentMode {
        CASH, CREDIT_CARD, DEBIT_CARD, ONLINE, BANK_TRANSFER, WALLET
    }

    public enum PaymentStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REFUNDED, PARTIALLY_REFUNDED
    }
}


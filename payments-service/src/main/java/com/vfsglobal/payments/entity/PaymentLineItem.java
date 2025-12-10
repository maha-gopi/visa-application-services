package com.vfsglobal.payments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payment_line_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLineItem {
    @Id
    @Column(name = "line_item_id")
    private UUID lineItemId;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(name = "item_type", nullable = false, length = 30)
    private String itemType;

    @Column(name = "item_description", nullable = false, length = 255)
    private String itemDescription;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reference_type", length = 30)
    private String referenceType;
}


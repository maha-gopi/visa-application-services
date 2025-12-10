package com.vfsglobal.bookings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @Column(name = "application_id")
    private UUID applicationId;

    @Column(name = "reference_number", nullable = false, length = 50, unique = true)
    private String referenceNumber;

    @Column(name = "applicant_id", nullable = false)
    private UUID applicantId;

    @Column(name = "agent_id")
    private UUID agentId;

    @Column(name = "visa_type_id", nullable = false)
    private UUID visaTypeId;

    @Column(name = "vac_id")
    private UUID vacId;

    @Column(name = "destination_country_id", nullable = false)
    private UUID destinationCountryId;

    @Column(name = "application_type", nullable = false, length = 20)
    private String applicationType = "NEW";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "application_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @Column(name = "sub_status", length = 50)
    private String subStatus;

    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft = true;

    @Column(name = "current_step", nullable = false)
    private Integer currentStep = 1;

    @Column(name = "total_steps", nullable = false)
    private Integer totalSteps = 5;

    @Column(name = "form_data_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String formDataJson;

    @Column(name = "travel_purpose", length = 255)
    private String travelPurpose;

    @Column(name = "intended_arrival_date")
    private LocalDate intendedArrivalDate;

    @Column(name = "intended_departure_date")
    private LocalDate intendedDepartureDate;

    @Column(name = "accommodation_details", columnDefinition = "TEXT")
    private String accommodationDetails;

    @Column(name = "submission_date")
    private OffsetDateTime submissionDate;

    @Column(name = "expiry_date")
    private OffsetDateTime expiryDate;

    @Column(name = "completed_date")
    private OffsetDateTime completedDate;

    @Column(name = "total_fee_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFeeAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "refunded_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @Column(name = "sla_due_date")
    private OffsetDateTime slaDueDate;

    @Column(name = "priority", nullable = false)
    private Integer priority = 5;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "cancelled_by_id")
    private UUID cancelledById;

    @Column(name = "cancelled_date")
    private OffsetDateTime cancelledDate;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private OffsetDateTime updatedDate;

    @Column(name = "created_by_user_type", nullable = false, length = 20)
    private String createdByUserType;

    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId;

    public enum ApplicationStatus {
        DRAFT, SUBMITTED, PAYMENT_PENDING, PAYMENT_COMPLETED,
        APPOINTMENT_SCHEDULED, CHECKED_IN, IN_PROGRESS,
        BIOMETRIC_CAPTURED, DATA_ENTERED, DOCUMENTS_SUBMITTED,
        QUALITY_CHECK, DISPATCHED, IN_TRANSIT,
        RECEIVED_BY_EMBASSY, UNDER_REVIEW,
        APPROVED, REJECTED, READY_FOR_COLLECTION, COLLECTED,
        CANCELLED, WITHDRAWN, EXPIRED
    }
}


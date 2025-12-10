package com.vfsglobal.appointments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "confirmation_number", nullable = false, length = 50, unique = true)
    private String confirmationNumber;

    @Column(name = "application_id", nullable = false, unique = true)
    private UUID applicationId;

    @Column(name = "slot_id", nullable = false)
    private UUID slotId;

    @Column(name = "vac_id", nullable = false)
    private UUID vacId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "appointment_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "booked_date", nullable = false)
    private OffsetDateTime bookedDate;

    @Column(name = "booked_by_user_type", nullable = false, length = 20)
    private String bookedByUserType;

    @Column(name = "booked_by_user_id", nullable = false)
    private UUID bookedByUserId;

    @Column(name = "check_in_time")
    private OffsetDateTime checkInTime;

    @Column(name = "check_in_by_staff_id")
    private UUID checkInByStaffId;

    @Column(name = "completion_time")
    private OffsetDateTime completionTime;

    @Column(name = "no_show_marked_time")
    private OffsetDateTime noShowMarkedTime;

    @Column(name = "rescheduled_from_id")
    private UUID rescheduledFromId;

    @Column(name = "reschedule_count", nullable = false)
    private Integer rescheduleCount = 0;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "cancelled_date")
    private OffsetDateTime cancelledDate;

    @Column(name = "reminder_sent_date")
    private OffsetDateTime reminderSentDate;

    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;

    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, CHECKED_IN, IN_PROGRESS,
        COMPLETED, CANCELLED, NO_SHOW, RESCHEDULED
    }
}


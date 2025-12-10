package com.vfsglobal.appointments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointment_slot", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"vac_id", "slot_date", "start_time"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlot {
    @Id
    @Column(name = "slot_id")
    private UUID slotId;

    @Column(name = "vac_id", nullable = false)
    private UUID vacId;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "total_quota", nullable = false)
    private Integer totalQuota;

    @Column(name = "booked_count", nullable = false)
    private Integer bookedCount = 0;

    @Column(name = "available_count", nullable = false)
    private Integer availableCount;

    @Column(name = "slot_type", nullable = false, length = 20)
    private String slotType = "REGULAR";

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}


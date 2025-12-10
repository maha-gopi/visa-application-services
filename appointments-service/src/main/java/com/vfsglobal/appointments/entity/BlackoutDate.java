package com.vfsglobal.appointments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "blackout_date", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"vac_id", "blackout_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlackoutDate {
    @Id
    @Column(name = "blackout_id")
    private UUID blackoutId;

    @Column(name = "vac_id", nullable = false)
    private UUID vacId;

    @Column(name = "blackout_date", nullable = false)
    private LocalDate blackoutDate;

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @Column(name = "blackout_type", nullable = false, length = 20)
    private String blackoutType = "FULL";

    @Column(name = "affected_slot_types", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String affectedSlotTypes;

    @Column(name = "created_by_staff_id", nullable = false)
    private UUID createdByStaffId;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}


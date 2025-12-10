package com.vfsglobal.utilities.entity;

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
@Table(name = "vac")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vac {
    @Id
    @Column(name = "vac_id")
    private UUID vacId;

    @Column(name = "vac_code", nullable = false, length = 20, unique = true)
    private String vacCode;

    @Column(name = "vac_name", nullable = false, length = 255)
    private String vacName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "fax", length = 20)
    private String fax;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone = "UTC";

    @Column(name = "operating_hours_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String operatingHoursJson;

    @Column(name = "max_daily_capacity")
    private Integer maxDailyCapacity;

    @Column(name = "default_slot_duration", nullable = false)
    private Integer defaultSlotDuration = 15;

    @Column(name = "allow_walk_ins", nullable = false)
    private Boolean allowWalkIns = true;

    @Column(name = "biometric_enabled", nullable = false)
    private Boolean biometricEnabled = true;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private OffsetDateTime updatedDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}


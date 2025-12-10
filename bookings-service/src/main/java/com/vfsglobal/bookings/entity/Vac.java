package com.vfsglobal.bookings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "country_id", nullable = false)
    private UUID countryId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}


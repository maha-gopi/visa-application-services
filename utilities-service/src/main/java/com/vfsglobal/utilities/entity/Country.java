package com.vfsglobal.utilities.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "country")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @Column(name = "country_id")
    private UUID countryId;

    @Column(name = "country_name", nullable = false, length = 100, unique = true)
    private String countryName;

    @Column(name = "country_code_2", nullable = false, length = 2, unique = true)
    private String countryCode2;

    @Column(name = "country_code_3", nullable = false, length = 3, unique = true)
    private String countryCode3;

    @Column(name = "numeric_code", length = 3)
    private String numericCode;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "sub_region", length = 100)
    private String subRegion;

    @Column(name = "calling_code", length = 10)
    private String callingCode;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "flag_icon_path", length = 255)
    private String flagIconPath;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 999;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private OffsetDateTime updatedDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}


package com.vfsglobal.utilities.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "applicant_photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantPhoto {
    @Id
    @Column(name = "photo_id")
    private UUID photoId;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "photo_type", nullable = false, length = 20)
    private String photoType = "UPLOADED";

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "original_file_name", length = 255)
    private String originalFileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_status", nullable = false)
    private ValidationStatus validationStatus = ValidationStatus.PENDING;

    @Column(name = "validation_errors", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String validationErrors;

    @Column(name = "validation_date")
    private OffsetDateTime validationDate;

    @Column(name = "captured_by_staff_id")
    private UUID capturedByStaffId;

    @Column(name = "captured_device_id")
    private UUID capturedDeviceId;

    @Column(name = "uploaded_date", nullable = false)
    private OffsetDateTime uploadedDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public enum ValidationStatus {
        PENDING, VERIFIED, REJECTED
    }
}


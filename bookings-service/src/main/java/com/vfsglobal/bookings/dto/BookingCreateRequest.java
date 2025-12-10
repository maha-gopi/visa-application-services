package com.vfsglobal.bookings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {
    private String visaTypeCode; // Logical ID: visa_type_code (e.g., VISA-STUDENT)
    private String applicantId; // Optional: UUID of existing applicant. If not provided, will lookup by passport number
    private PersonalDetails personalDetails;
    private String photo;
    private String appointmentDate;
    private String appointmentTime;
    private List<String> selectedServices; // Logical IDs: service codes (e.g., VAS-KEEP-PASSPORT)
    private String locationCode; // Logical ID: vac_code (e.g., LOC-NY-MAIN)
}


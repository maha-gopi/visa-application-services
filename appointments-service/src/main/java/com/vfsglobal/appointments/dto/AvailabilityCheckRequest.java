package com.vfsglobal.appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityCheckRequest {
    private String date;
    private String time;
    private String visaTypeCode; // Logical ID: visa_type_code
    private String locationCode; // Logical ID: vac_code
}


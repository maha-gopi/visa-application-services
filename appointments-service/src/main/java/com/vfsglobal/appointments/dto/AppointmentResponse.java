package com.vfsglobal.appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;
    private String date;
    private String time;
    private String status;
    private String locationId;
    private String locationName;
    private String bookingId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}


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
public class BookingUpdateRequest {
    private PersonalDetails personalDetails;
    private String appointmentDate;
    private String appointmentTime;
    private List<String> selectedServices;
}


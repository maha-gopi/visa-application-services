package com.vfsglobal.bookings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetails {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String passportExpiry;
    private String email;
    private String phone;
    private String address;
}


package com.vfsglobal.utilities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {
    private String id;
    private String name;
    private LocationAddress address;
    private LocationCoordinates coordinates;
    private String phone;
    private String email;
    private OperatingHours operatingHours;
    private List<String> services;
    private Boolean active;
}


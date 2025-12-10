package com.vfsglobal.visatypes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisaTypeResponse {
    private String id; // Logical ID: visa_type_code
    private String visaCode;
    private String name;
    private String description;
    private String duration;
    private BigDecimal fee;
    private String currency;
    private String icon;
    private List<String> requirements;
    private String processingTime;
    private Boolean active;
}



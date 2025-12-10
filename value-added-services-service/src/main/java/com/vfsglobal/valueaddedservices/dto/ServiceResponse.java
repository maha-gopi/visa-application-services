package com.vfsglobal.valueaddedservices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private String icon;
    private String category;
    private List<String> features;
    private Boolean active;
}



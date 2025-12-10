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
public class PhotoValidationResponse {
    private Boolean valid;
    private String message;
    private ValidationDetails validationDetails;
    private List<ValidationError> errors;
}


package com.vfsglobal.utilities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationDetails {
    private String format;
    private Integer size;
    private PhotoDimensions dimensions;
    private String quality;
}


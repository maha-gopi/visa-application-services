package com.vfsglobal.valueaddedservices.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBatchRequest {
    @NotEmpty
    @Size(min = 1, max = 50)
    private List<String> serviceIds; // Logical IDs: service codes (e.g., VAS-KEEP-PASSPORT)
}



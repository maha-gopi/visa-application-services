package com.vfsglobal.visatypes.service;

import com.vfsglobal.visatypes.dto.VisaTypeListResponse;
import com.vfsglobal.visatypes.dto.VisaTypeResponse;
import com.vfsglobal.visatypes.entity.VisaType;
import com.vfsglobal.visatypes.repository.VisaTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisaTypeService {

    private final VisaTypeRepository visaTypeRepository;

    @Transactional(readOnly = true)
    public VisaTypeListResponse listVisaTypes(String countryCode, Boolean active, String sortBy, String sortOrder) {
        log.debug("Listing visa types with filters: countryCode={}, active={}, sortBy={}, sortOrder={}", 
                countryCode, active, sortBy, sortOrder);

        List<VisaType> visaTypes;
        Boolean activeFilter = active != null ? active : true;

        if (countryCode != null && !countryCode.isEmpty()) {
            // Look up country by code first, then filter visa types by country
            visaTypes = visaTypeRepository.findByCountryCodeAndActive(countryCode, activeFilter);
        } else {
            visaTypes = visaTypeRepository.findByActive(activeFilter);
        }

        // Apply sorting
        if (sortBy != null) {
            visaTypes = sortVisaTypes(visaTypes, sortBy, sortOrder);
        }

        List<VisaTypeResponse> responseList = visaTypes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return VisaTypeListResponse.builder()
                .visaTypes(responseList)
                .build();
    }

    @Transactional(readOnly = true)
    public VisaTypeResponse getVisaTypeById(String visaTypeCode) {
        log.debug("Getting visa type by code: {}", visaTypeCode);
        
        // Accept logical ID (visa_type_code) instead of UUID
        VisaType visaType = visaTypeRepository.findByVisaTypeCode(visaTypeCode)
                .orElseThrow(() -> new RuntimeException("Visa type not found: " + visaTypeCode));

        return mapToResponse(visaType);
    }

    private VisaTypeResponse mapToResponse(VisaType visaType) {
        String duration = null;
        if (visaType.getValidityDays() != null && visaType.getMaxStayDays() != null) {
            duration = visaType.getValidityDays() + "-" + visaType.getMaxStayDays() + " days";
        } else if (visaType.getValidityDays() != null) {
            duration = visaType.getValidityDays() + " days";
        }

        String processingTime = null;
        if (visaType.getProcessingTimeDays() != null) {
            processingTime = visaType.getProcessingTimeDays() + " business days";
        }

        return VisaTypeResponse.builder()
                .id(visaType.getVisaTypeCode()) // Expose logical ID instead of UUID
                .visaCode(visaType.getVisaTypeCode())
                .name(visaType.getVisaTypeName())
                .description(visaType.getDescription())
                .duration(duration)
                .fee(visaType.getBaseFee())
                .currency(visaType.getCurrency())
                .icon("✈️") // Default icon, could be stored in DB
                .processingTime(processingTime)
                .active(visaType.getIsActive())
                .build();
    }

    private List<VisaType> sortVisaTypes(List<VisaType> visaTypes, String sortBy, String sortOrder) {
        boolean ascending = sortOrder == null || !sortOrder.equalsIgnoreCase("desc");
        
        return visaTypes.stream()
                .sorted((a, b) -> {
                    int comparison = 0;
                    switch (sortBy.toLowerCase()) {
                        case "name":
                            comparison = a.getVisaTypeName().compareTo(b.getVisaTypeName());
                            break;
                        case "fee":
                            comparison = a.getBaseFee().compareTo(b.getBaseFee());
                            break;
                        case "duration":
                            Integer aDuration = a.getValidityDays() != null ? a.getValidityDays() : 0;
                            Integer bDuration = b.getValidityDays() != null ? b.getValidityDays() : 0;
                            comparison = aDuration.compareTo(bDuration);
                            break;
                        default:
                            comparison = a.getVisaTypeName().compareTo(b.getVisaTypeName());
                    }
                    return ascending ? comparison : -comparison;
                })
                .collect(Collectors.toList());
    }
}



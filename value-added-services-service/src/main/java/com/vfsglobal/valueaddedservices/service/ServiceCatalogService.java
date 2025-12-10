package com.vfsglobal.valueaddedservices.service;

import com.vfsglobal.valueaddedservices.dto.ServiceBatchRequest;
import com.vfsglobal.valueaddedservices.dto.ServiceListResponse;
import com.vfsglobal.valueaddedservices.dto.ServiceResponse;
import com.vfsglobal.valueaddedservices.entity.ServiceCatalog;
import com.vfsglobal.valueaddedservices.repository.ServiceCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCatalogService {

    private final ServiceCatalogRepository serviceCatalogRepository;

    @Transactional(readOnly = true)
    public ServiceListResponse listServices(String category, Boolean active, String sortBy, String sortOrder) {
        log.debug("Listing services with filters: category={}, active={}, sortBy={}, sortOrder={}", 
                category, active, sortBy, sortOrder);

        List<ServiceCatalog> services;
        Boolean activeFilter = active != null ? active : true;

        if (category != null && !category.isEmpty()) {
            services = serviceCatalogRepository.findByCategoryAndActive(category, activeFilter);
        } else {
            services = serviceCatalogRepository.findByActive(activeFilter);
        }

        // Apply sorting
        if (sortBy != null) {
            services = sortServices(services, sortBy, sortOrder);
        }

        List<ServiceResponse> responseList = services.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ServiceListResponse.builder()
                .services(responseList)
                .build();
    }

    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(String serviceId) {
        log.debug("Getting service by ID: {}", serviceId);
        
        ServiceCatalog service = serviceCatalogRepository.findByServiceCode(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found: " + serviceId));

        return mapToResponse(service);
    }

    @Transactional(readOnly = true)
    public ServiceListResponse getServicesBatch(ServiceBatchRequest request) {
        log.debug("Getting services batch: {}", request.getServiceIds());
        
        List<ServiceCatalog> services = serviceCatalogRepository.findByServiceCodes(request.getServiceIds());
        
        List<ServiceResponse> responseList = services.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ServiceListResponse.builder()
                .services(responseList)
                .build();
    }

    private ServiceResponse mapToResponse(ServiceCatalog service) {
        return ServiceResponse.builder()
                .id(service.getServiceCode())
                .name(service.getServiceName())
                .description(service.getDescription())
                .price(service.getBasePrice())
                .currency(service.getCurrency())
                .icon("📝") // Default icon
                .category(service.getCategory())
                .active(service.getIsActive())
                .build();
    }

    private List<ServiceCatalog> sortServices(List<ServiceCatalog> services, String sortBy, String sortOrder) {
        boolean ascending = sortOrder == null || !sortOrder.equalsIgnoreCase("desc");
        
        return services.stream()
                .sorted((a, b) -> {
                    int comparison = 0;
                    switch (sortBy.toLowerCase()) {
                        case "name":
                            comparison = a.getServiceName().compareTo(b.getServiceName());
                            break;
                        case "price":
                            comparison = a.getBasePrice().compareTo(b.getBasePrice());
                            break;
                        case "category":
                            comparison = (a.getCategory() != null ? a.getCategory() : "").compareTo(
                                    b.getCategory() != null ? b.getCategory() : "");
                            break;
                        default:
                            comparison = a.getServiceName().compareTo(b.getServiceName());
                    }
                    return ascending ? comparison : -comparison;
                })
                .collect(Collectors.toList());
    }
}



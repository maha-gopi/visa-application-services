package com.vfsglobal.valueaddedservices.controller;

import com.vfsglobal.valueaddedservices.dto.ServiceBatchRequest;
import com.vfsglobal.valueaddedservices.dto.ServiceListResponse;
import com.vfsglobal.valueaddedservices.dto.ServiceResponse;
import com.vfsglobal.valueaddedservices.service.ServiceCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Services", description = "Endpoints for managing value-added services")
public class ServiceController {

    private final ServiceCatalogService serviceCatalogService;

    @GetMapping
    @Operation(summary = "List all available value-added services")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful response"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ServiceListResponse> listServices(
            @Parameter(description = "Filter by service category")
            @RequestParam(required = false) String category,
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Field to sort by")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort order")
            @RequestParam(required = false) String sortOrder) {
        
        ServiceListResponse response = serviceCatalogService.listServices(category, active, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serviceCode}")
    @Operation(summary = "Get service by code", description = "Retrieves service details using business code (e.g., VAS-KEEP-PASSPORT)")
    public ResponseEntity<ServiceResponse> getServiceByCode(
            @Parameter(description = "Business code for the service", example = "VAS-KEEP-PASSPORT")
            @PathVariable String serviceCode) {
        ServiceResponse response = serviceCatalogService.getServiceById(serviceCode);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    @Operation(summary = "Get multiple services by IDs")
    public ResponseEntity<ServiceListResponse> getServicesBatch(@Valid @RequestBody ServiceBatchRequest request) {
        ServiceListResponse response = serviceCatalogService.getServicesBatch(request);
        return ResponseEntity.ok(response);
    }
}



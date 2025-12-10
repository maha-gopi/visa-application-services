package com.vfsglobal.visatypes.controller;

import com.vfsglobal.visatypes.dto.VisaTypeListResponse;
import com.vfsglobal.visatypes.dto.VisaTypeResponse;
import com.vfsglobal.visatypes.service.VisaTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visa-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Visa Types", description = "Endpoints for managing visa types and their configurations")
public class VisaTypeController {

    private final VisaTypeService visaTypeService;

    @GetMapping
    @Operation(summary = "List all available visa types", 
               description = "Retrieves a list of all available visa types with their details, fees, and requirements")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = VisaTypeListResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VisaTypeListResponse> listVisaTypes(
            @Parameter(description = "Filter by destination country code (ISO 3166-1 alpha-2)", example = "US")
            @RequestParam(required = false) String country,
            @Parameter(description = "Filter by active status", example = "true")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort order", example = "asc")
            @RequestParam(required = false) String sortOrder) {
        
        log.info("Listing visa types - country: {}, active: {}, sortBy: {}, sortOrder: {}", 
                country, active, sortBy, sortOrder);
        
        VisaTypeListResponse response = visaTypeService.listVisaTypes(country, active, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{visaTypeCode}")
    @Operation(summary = "Get visa type by code", 
               description = "Retrieves detailed information about a specific visa type using its business code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = VisaTypeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Visa type not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VisaTypeResponse> getVisaTypeByCode(
            @Parameter(description = "Business code for the visa type (e.g., VISA-STUDENT)", example = "VISA-STUDENT")
            @PathVariable String visaTypeCode) {
        
        log.info("Getting visa type by code: {}", visaTypeCode);
        
        VisaTypeResponse response = visaTypeService.getVisaTypeById(visaTypeCode);
        return ResponseEntity.ok(response);
    }
}



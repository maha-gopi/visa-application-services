package com.vfsglobal.utilities.controller;

import com.vfsglobal.utilities.dto.*;
import com.vfsglobal.utilities.service.UtilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Utilities", description = "API endpoints for utility functions including nationalities, photo validation, and location information")
public class UtilityController {
    
    private final UtilityService utilityService;
    
    @GetMapping("/nationalities")
    @Operation(summary = "List all supported nationalities", description = "Retrieves a list of all supported nationalities with country codes and names")
    public ResponseEntity<NationalitiesResponse> listNationalities(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.debug("Listing nationalities with search: {}", search);
        NationalitiesResponse response = utilityService.listNationalities(search, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/photos/validate")
    @Operation(summary = "Validate photo", description = "Validates a passport photo against VFS requirements")
    public ResponseEntity<PhotoValidationResponse> validatePhoto(
            @RequestBody PhotoValidationRequest request) {
        log.debug("Validating photo");
        PhotoValidationResponse response = utilityService.validatePhoto(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/photos/upload")
    @Operation(summary = "Upload photo", description = "Uploads a validated passport photo and returns a URL for the stored photo")
    public ResponseEntity<PhotoUploadResponse> uploadPhoto(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam(required = false) String bookingId) {
        log.debug("Uploading photo for booking: {}", bookingId);
        PhotoUploadResponse response = utilityService.uploadPhoto(bookingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/locations")
    @Operation(summary = "List VFS locations", description = "Retrieves a list of all VFS application centers and locations")
    public ResponseEntity<LocationsResponse> listLocations(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.debug("Listing locations with filters");
        LocationsResponse response = utilityService.listLocations(country, city, search, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/locations/{locationCode}")
    @Operation(summary = "Get location by code", description = "Retrieves detailed information about a specific VFS location using its business code (e.g., LOC-NY-MAIN)")
    public ResponseEntity<LocationResponse> getLocationByCode(
            @io.swagger.v3.oas.annotations.Parameter(description = "Business code for the location", example = "LOC-NY-MAIN")
            @PathVariable String locationCode) {
        log.debug("Getting location by code {}", locationCode);
        LocationResponse response = utilityService.getLocationById(locationCode);
        return ResponseEntity.ok(response);
    }
}


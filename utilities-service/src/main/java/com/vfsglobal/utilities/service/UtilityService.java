package com.vfsglobal.utilities.service;

import com.vfsglobal.utilities.dto.*;
import com.vfsglobal.utilities.entity.Country;
import com.vfsglobal.utilities.entity.Vac;
import com.vfsglobal.utilities.repository.CountryRepository;
import com.vfsglobal.utilities.repository.VacRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilityService {
    
    private final CountryRepository countryRepository;
    private final VacRepository vacRepository;
    
    public NationalitiesResponse listNationalities(String search, String sortBy, String sortOrder) {
        List<Country> countries;
        if (search != null && !search.isEmpty()) {
            countries = countryRepository.searchActive(search);
        } else {
            countries = countryRepository.findAllActive();
        }
        
        List<NationalityResponse> nationalities = countries.stream()
            .map(country -> NationalityResponse.builder()
                .code(country.getCountryCode2())
                .name(country.getCountryName())
                .flag("🇺🇸") // Should be derived from flag_icon_path
                .build())
            .collect(Collectors.toList());
        
        return NationalitiesResponse.builder()
            .nationalities(nationalities)
            .build();
    }
    
    public PhotoValidationResponse validatePhoto(PhotoValidationRequest request) {
        // Simulate photo validation
        // In real implementation, this would decode base64, check dimensions, size, format, etc.
        
        return PhotoValidationResponse.builder()
            .valid(true)
            .message("Photo meets all requirements")
            .validationDetails(ValidationDetails.builder()
                .format("JPEG")
                .size(1024000)
                .dimensions(PhotoDimensions.builder()
                    .width(413)
                    .height(531)
                    .build())
                .quality("good")
                .build())
            .build();
    }
    
    public PhotoUploadResponse uploadPhoto(String bookingId) {
        // Simulate photo upload
        // In real implementation, this would save the file to storage and return URLs
        
        return PhotoUploadResponse.builder()
            .photoId("photo-" + UUID.randomUUID().toString().substring(0, 8))
            .url("https://storage.vfsglobal.com/photos/photo-123456.jpg")
            .thumbnailUrl("https://storage.vfsglobal.com/photos/thumbnails/photo-123456.jpg")
            .uploadedAt(java.time.OffsetDateTime.now())
            .build();
    }
    
    public LocationsResponse listLocations(String country, String city, String search, String sortBy, String sortOrder) {
        List<Vac> vacs;
        
        if (country != null) {
            vacs = vacRepository.findByCountry(country);
        } else if (city != null) {
            vacs = vacRepository.findByCity(city);
        } else if (search != null && !search.isEmpty()) {
            vacs = vacRepository.search(search);
        } else {
            vacs = vacRepository.findAllActive();
        }
        
        List<LocationResponse> locations = vacs.stream()
            .map(this::mapToLocationResponse)
            .collect(Collectors.toList());
        
        return LocationsResponse.builder()
            .locations(locations)
            .build();
    }
    
    public LocationResponse getLocationById(String locationCode) {
        // Accept logical ID (vac_code) instead of UUID
        Vac vac = vacRepository.findByVacCode(locationCode)
            .orElseThrow(() -> new RuntimeException("Location not found: " + locationCode));
        return mapToLocationResponse(vac);
    }
    
    private LocationResponse mapToLocationResponse(Vac vac) {
        return LocationResponse.builder()
            .id(vac.getVacCode()) // Expose logical ID instead of UUID
            .name(vac.getVacName())
            .address(LocationAddress.builder()
                .street(vac.getAddress())
                .city(vac.getCity())
                .state(vac.getState())
                .postalCode(vac.getPostalCode())
                .country(vac.getCountry() != null ? vac.getCountry().getCountryCode2() : null)
                .build())
            .coordinates(vac.getLatitude() != null && vac.getLongitude() != null ?
                LocationCoordinates.builder()
                    .latitude(vac.getLatitude().doubleValue())
                    .longitude(vac.getLongitude().doubleValue())
                    .build() : null)
            .phone(vac.getContactPhone())
            .email(vac.getContactEmail())
            .active(vac.getIsActive())
            .build();
    }
}


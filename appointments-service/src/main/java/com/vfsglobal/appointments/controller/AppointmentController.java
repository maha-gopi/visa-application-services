package com.vfsglobal.appointments.controller;

import com.vfsglobal.appointments.dto.*;
import com.vfsglobal.appointments.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Appointments", description = "API endpoints for managing appointment scheduling, availability, and time slots")
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    
    @GetMapping("/available-dates")
    @Operation(summary = "Get available appointment dates", description = "Retrieves a list of available dates for booking appointments within a specified date range")
    public ResponseEntity<AvailableDatesResponse> getAvailableDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
            @RequestParam(required = false) String visaTypeCode,
            @RequestParam(required = false) String locationCode) {
        log.debug("Getting available dates from {} to {}", startDate, endDate);
        AvailableDatesResponse response = appointmentService.getAvailableDates(startDate, endDate, visaTypeCode, locationCode);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/available-time-slots")
    @Operation(summary = "Get available time slots for a date", description = "Retrieves available time slots for a specific date")
    public ResponseEntity<AvailableTimeSlotsResponse> getAvailableTimeSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date,
            @RequestParam(required = false) String visaTypeCode,
            @RequestParam(required = false) String locationCode) {
        log.debug("Getting available time slots for date {}", date);
        AvailableTimeSlotsResponse response = appointmentService.getAvailableTimeSlots(date, visaTypeCode, locationCode);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/check-availability")
    @Operation(summary = "Check specific date and time availability", description = "Checks if a specific date and time slot is available for booking")
    public ResponseEntity<AvailabilityCheckResponse> checkAvailability(@RequestBody AvailabilityCheckRequest request) {
        log.debug("Checking availability for date {} and time {}", request.getDate(), request.getTime());
        AvailabilityCheckResponse response = appointmentService.checkAvailability(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{confirmationNumber}")
    @Operation(summary = "Get appointment details", description = "Retrieves detailed information about a specific appointment using confirmation number")
    public ResponseEntity<AppointmentResponse> getAppointmentByConfirmationNumber(
            @io.swagger.v3.oas.annotations.Parameter(description = "Appointment confirmation number", example = "APT-20241208-001")
            @PathVariable String confirmationNumber) {
        log.debug("Getting appointment by confirmation number {}", confirmationNumber);
        AppointmentResponse response = appointmentService.getAppointmentById(confirmationNumber);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{confirmationNumber}")
    @Operation(summary = "Update appointment", description = "Updates an existing appointment (e.g., reschedule) using confirmation number")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @io.swagger.v3.oas.annotations.Parameter(description = "Appointment confirmation number", example = "APT-20241208-001")
            @PathVariable String confirmationNumber,
            @RequestBody AppointmentUpdateRequest request) {
        log.debug("Updating appointment {}", confirmationNumber);
        AppointmentResponse response = appointmentService.updateAppointment(confirmationNumber, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{confirmationNumber}")
    @Operation(summary = "Cancel appointment", description = "Cancels an existing appointment using confirmation number")
    public ResponseEntity<Void> cancelAppointment(
            @io.swagger.v3.oas.annotations.Parameter(description = "Appointment confirmation number", example = "APT-20241208-001")
            @PathVariable String confirmationNumber,
            @RequestParam(required = false) String reason) {
        log.debug("Cancelling appointment {}", confirmationNumber);
        appointmentService.cancelAppointment(confirmationNumber, reason);
        return ResponseEntity.noContent().build();
    }
}


package com.vfsglobal.bookings.controller;

import com.vfsglobal.bookings.dto.*;
import com.vfsglobal.bookings.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bookings", description = "API endpoints for managing visa application bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    @Operation(summary = "Create a new booking", description = "Creates a new visa application booking with all required details")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingCreateRequest request) {
        log.debug("Creating new booking");
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "List bookings", description = "Retrieves a list of bookings with optional filtering and pagination")
    public ResponseEntity<Page<BookingSummary>> listBookings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String visaTypeCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        log.debug("Listing bookings with filters");
        Page<BookingSummary> response = bookingService.listBookings(status, visaTypeCode, startDate, endDate, 
            page, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{referenceNumber}")
    @Operation(summary = "Get booking by reference number", description = "Retrieves detailed information about a specific booking using reference number")
    public ResponseEntity<BookingResponse> getBookingByReference(
            @io.swagger.v3.oas.annotations.Parameter(description = "Booking reference number", example = "APP20241208000001")
            @PathVariable String referenceNumber) {
        log.debug("Getting booking by reference {}", referenceNumber);
        BookingResponse response = bookingService.getBookingById(referenceNumber);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{referenceNumber}")
    @Operation(summary = "Update booking", description = "Updates an existing booking using reference number (limited fields can be updated)")
    public ResponseEntity<BookingResponse> updateBooking(
            @io.swagger.v3.oas.annotations.Parameter(description = "Booking reference number", example = "APP20241208000001")
            @PathVariable String referenceNumber,
            @RequestBody BookingUpdateRequest request) {
        log.debug("Updating booking {}", referenceNumber);
        BookingResponse response = bookingService.updateBooking(referenceNumber, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{referenceNumber}")
    @Operation(summary = "Cancel booking", description = "Cancels an existing booking using reference number")
    public ResponseEntity<Void> cancelBooking(
            @io.swagger.v3.oas.annotations.Parameter(description = "Booking reference number", example = "APP20241208000001")
            @PathVariable String referenceNumber,
            @RequestParam(required = false) String reason) {
        log.debug("Cancelling booking {}", referenceNumber);
        bookingService.cancelBooking(referenceNumber, reason);
        return ResponseEntity.noContent().build();
    }
}


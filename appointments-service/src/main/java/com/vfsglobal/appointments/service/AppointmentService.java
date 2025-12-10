package com.vfsglobal.appointments.service;

import com.vfsglobal.appointments.dto.*;
import com.vfsglobal.appointments.entity.Appointment;
import com.vfsglobal.appointments.entity.AppointmentSlot;
import com.vfsglobal.appointments.entity.BlackoutDate;
import com.vfsglobal.appointments.repository.AppointmentRepository;
import com.vfsglobal.appointments.repository.AppointmentSlotRepository;
import com.vfsglobal.appointments.repository.BlackoutDateRepository;
import com.vfsglobal.appointments.repository.VacRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final BlackoutDateRepository blackoutDateRepository;
    private final VacRepository vacRepository;
    
    public AvailableDatesResponse getAvailableDates(String startDate, String endDate, String visaTypeCode, String locationCode) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        // Translate logical ID (vac_code) to UUID
        UUID vacId = null;
        if (locationCode != null) {
            vacId = vacRepository.findByVacCode(locationCode)
                .map(vac -> vac.getVacId())
                .orElseThrow(() -> new RuntimeException("Location not found: " + locationCode));
        }
        
        List<LocalDate> availableDates = appointmentSlotRepository.findAvailableDatesByVacAndDateRange(vacId, start, end);
        
        // Filter out blackout dates
        if (vacId != null) {
            List<BlackoutDate> blackouts = blackoutDateRepository.findBlackoutDatesByVacAndDateRange(vacId, start, end);
            List<LocalDate> blackoutDates = blackouts.stream()
                .map(BlackoutDate::getBlackoutDate)
                .collect(Collectors.toList());
            availableDates = availableDates.stream()
                .filter(date -> !blackoutDates.contains(date))
                .collect(Collectors.toList());
        }
        
        List<String> dateStrings = availableDates.stream()
            .map(LocalDate::toString)
            .collect(Collectors.toList());
        
        return AvailableDatesResponse.builder()
            .availableDates(dateStrings)
            .build();
    }
    
    public AvailableTimeSlotsResponse getAvailableTimeSlots(String date, String visaTypeCode, String locationCode) {
        LocalDate slotDate = LocalDate.parse(date);
        
        // Translate logical ID (vac_code) to UUID
        UUID vacId = null;
        if (locationCode != null) {
            vacId = vacRepository.findByVacCode(locationCode)
                .map(vac -> vac.getVacId())
                .orElseThrow(() -> new RuntimeException("Location not found: " + locationCode));
        }
        
        List<AppointmentSlot> slots = appointmentSlotRepository.findByVacIdAndSlotDate(vacId, slotDate);
        
        // Filter out blackout dates
        if (vacId != null) {
            Optional<BlackoutDate> blackout = blackoutDateRepository.findByVacIdAndBlackoutDate(vacId, slotDate);
            if (blackout.isPresent() && "FULL".equals(blackout.get().getBlackoutType())) {
                return AvailableTimeSlotsResponse.builder()
                    .date(date)
                    .timeSlots(new ArrayList<>())
                    .build();
            }
        }
        
        List<TimeSlotResponse> timeSlots = slots.stream()
            .filter(slot -> slot.getIsActive() && slot.getAvailableCount() > 0)
            .map(slot -> TimeSlotResponse.builder()
                .time(slot.getStartTime().toString())
                .available(slot.getAvailableCount() > 0)
                .slotId(slot.getSlotId().toString())
                .capacity(slot.getAvailableCount())
                .build())
            .collect(Collectors.toList());
        
        return AvailableTimeSlotsResponse.builder()
            .date(date)
            .timeSlots(timeSlots)
            .build();
    }
    
    public AvailabilityCheckResponse checkAvailability(AvailabilityCheckRequest request) {
        LocalDate checkDate = LocalDate.parse(request.getDate());
        LocalTime checkTime = LocalTime.parse(request.getTime());
        
        // Translate logical ID (vac_code) to UUID
        UUID vacId = null;
        if (request.getLocationCode() != null) {
            vacId = vacRepository.findByVacCode(request.getLocationCode())
                .map(vac -> vac.getVacId())
                .orElseThrow(() -> new RuntimeException("Location not found: " + request.getLocationCode()));
        }
        
        // Check blackout dates
        if (vacId != null) {
            Optional<BlackoutDate> blackout = blackoutDateRepository.findByVacIdAndBlackoutDate(vacId, checkDate);
            if (blackout.isPresent() && "FULL".equals(blackout.get().getBlackoutType())) {
                return AvailabilityCheckResponse.builder()
                    .available(false)
                    .date(request.getDate())
                    .time(request.getTime())
                    .message("Date is blocked")
                    .build();
            }
        }
        
        // Check slot availability
        Optional<AppointmentSlot> slot = appointmentSlotRepository.findByVacIdAndSlotDateAndStartTime(vacId, checkDate, checkTime);
        if (slot.isPresent() && slot.get().getAvailableCount() > 0 && slot.get().getIsActive()) {
            return AvailabilityCheckResponse.builder()
                .available(true)
                .date(request.getDate())
                .time(request.getTime())
                .message("Slot is available")
                .build();
        }
        
        return AvailabilityCheckResponse.builder()
            .available(false)
            .date(request.getDate())
            .time(request.getTime())
            .message("Slot is not available")
            .build();
    }
    
    public AppointmentResponse getAppointmentById(String confirmationNumber) {
        // Accept logical ID (confirmation_number) instead of UUID
        Appointment appointment = appointmentRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + confirmationNumber));
        
        return mapToResponse(appointment);
    }
    
    @Transactional
    public AppointmentResponse updateAppointment(String confirmationNumber, AppointmentUpdateRequest request) {
        // Accept logical ID (confirmation_number) instead of UUID
        Appointment appointment = appointmentRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + confirmationNumber));
        
        if (request.getDate() != null) {
            appointment.setAppointmentDate(LocalDate.parse(request.getDate()));
        }
        if (request.getTime() != null) {
            appointment.setAppointmentTime(LocalTime.parse(request.getTime()));
        }
        if (request.getLocationCode() != null) {
            // Translate logical ID (vac_code) to UUID
            UUID vacId = vacRepository.findByVacCode(request.getLocationCode())
                .map(vac -> vac.getVacId())
                .orElseThrow(() -> new RuntimeException("Location not found: " + request.getLocationCode()));
            appointment.setVacId(vacId);
        }
        
        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }
    
    @Transactional
    public void cancelAppointment(String confirmationNumber, String reason) {
        // Accept logical ID (confirmation_number) instead of UUID
        Appointment appointment = appointmentRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + confirmationNumber));
        
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);
        appointment.setCancelledDate(OffsetDateTime.now());
        
        // Release the slot
        AppointmentSlot slot = appointmentSlotRepository.findBySlotId(appointment.getSlotId())
            .orElse(null);
        if (slot != null) {
            slot.setBookedCount(slot.getBookedCount() - 1);
            slot.setAvailableCount(slot.getAvailableCount() + 1);
            appointmentSlotRepository.save(slot);
        }
        
        appointmentRepository.save(appointment);
    }
    
    private AppointmentResponse mapToResponse(Appointment appointment) {
        String status = appointment.getStatus().name().toLowerCase();
        if (status.equals("no_show")) {
            status = "no-show";
        }
        
        // Get location code for response
        String locationCode = vacRepository.findByVacId(appointment.getVacId())
            .map(vac -> vac.getVacCode())
            .orElse(null);
        
        return AppointmentResponse.builder()
            .id(appointment.getConfirmationNumber()) // Expose logical ID instead of UUID
            .date(appointment.getAppointmentDate().toString())
            .time(appointment.getAppointmentTime().toString())
            .status(status)
            .locationId(locationCode) // Expose logical ID instead of UUID
            .bookingId(appointment.getApplicationId().toString()) // Keep UUID for now, will update when bookings-service is done
            .createdAt(appointment.getBookedDate())
            .updatedAt(appointment.getBookedDate())
            .build();
    }
}


package com.vfsglobal.appointments.repository;

import com.vfsglobal.appointments.entity.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, UUID> {
    Optional<AppointmentSlot> findBySlotId(UUID slotId);
    
    List<AppointmentSlot> findByVacIdAndSlotDate(UUID vacId, LocalDate slotDate);
    
    @Query("SELECT s FROM AppointmentSlot s WHERE s.vacId = :vacId AND s.slotDate BETWEEN :startDate AND :endDate AND s.availableCount > 0 AND s.isActive = true")
    List<AppointmentSlot> findAvailableSlotsByVacAndDateRange(
        @Param("vacId") UUID vacId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT DISTINCT s.slotDate FROM AppointmentSlot s WHERE s.vacId = :vacId AND s.slotDate BETWEEN :startDate AND :endDate AND s.availableCount > 0 AND s.isActive = true ORDER BY s.slotDate")
    List<LocalDate> findAvailableDatesByVacAndDateRange(
        @Param("vacId") UUID vacId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    Optional<AppointmentSlot> findByVacIdAndSlotDateAndStartTime(UUID vacId, LocalDate slotDate, LocalTime startTime);
}


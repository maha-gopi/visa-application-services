package com.vfsglobal.appointments.repository;

import com.vfsglobal.appointments.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findByAppointmentId(UUID appointmentId);
    
    Optional<Appointment> findByConfirmationNumber(String confirmationNumber);
    
    Optional<Appointment> findByApplicationId(UUID applicationId);
    
    List<Appointment> findByVacIdAndAppointmentDate(UUID vacId, LocalDate appointmentDate);
    
    @Query("SELECT a FROM Appointment a WHERE a.vacId = :vacId AND a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findAppointmentsByVacAndDateRange(
        @Param("vacId") UUID vacId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}


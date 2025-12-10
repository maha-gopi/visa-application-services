package com.vfsglobal.appointments.repository;

import com.vfsglobal.appointments.entity.BlackoutDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlackoutDateRepository extends JpaRepository<BlackoutDate, UUID> {
    Optional<BlackoutDate> findByBlackoutId(UUID blackoutId);
    
    @Query("SELECT b FROM BlackoutDate b WHERE b.vacId = :vacId AND b.blackoutDate BETWEEN :startDate AND :endDate AND b.isActive = true")
    List<BlackoutDate> findBlackoutDatesByVacAndDateRange(
        @Param("vacId") UUID vacId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    Optional<BlackoutDate> findByVacIdAndBlackoutDate(UUID vacId, LocalDate blackoutDate);
}


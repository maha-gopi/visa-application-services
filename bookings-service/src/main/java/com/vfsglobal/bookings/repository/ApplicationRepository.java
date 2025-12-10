package com.vfsglobal.bookings.repository;

import com.vfsglobal.bookings.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Optional<Application> findByApplicationId(UUID applicationId);
    
    Optional<Application> findByReferenceNumber(String referenceNumber);
    
    Page<Application> findByStatus(Application.ApplicationStatus status, Pageable pageable);
    
    @Query("SELECT a FROM Application a WHERE " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:visaTypeId IS NULL OR a.visaTypeId = :visaTypeId) AND " +
           "(:startDate IS NULL OR a.createdDate >= :startDate) AND " +
           "(:endDate IS NULL OR a.createdDate <= :endDate)")
    Page<Application> findApplicationsWithFilters(
        @Param("status") Application.ApplicationStatus status,
        @Param("visaTypeId") UUID visaTypeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );
}


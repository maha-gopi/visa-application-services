package com.vfsglobal.bookings.repository;

import com.vfsglobal.bookings.entity.VisaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisaTypeRepository extends JpaRepository<VisaType, UUID> {
    @Query("SELECT vt FROM VisaType vt WHERE vt.visaTypeCode = :code AND vt.isActive = true")
    Optional<VisaType> findByVisaTypeCode(@Param("code") String code);
}


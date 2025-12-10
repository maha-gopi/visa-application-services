package com.vfsglobal.bookings.repository;

import com.vfsglobal.bookings.entity.ApplicationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationServiceRepository extends JpaRepository<ApplicationService, UUID> {
    List<ApplicationService> findByApplicationId(UUID applicationId);
}


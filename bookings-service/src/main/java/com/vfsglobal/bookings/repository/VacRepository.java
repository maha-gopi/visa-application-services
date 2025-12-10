package com.vfsglobal.bookings.repository;

import com.vfsglobal.bookings.entity.Vac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VacRepository extends JpaRepository<Vac, UUID> {
    Optional<Vac> findByVacCode(String vacCode);
}


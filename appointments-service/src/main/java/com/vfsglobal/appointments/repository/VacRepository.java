package com.vfsglobal.appointments.repository;

import com.vfsglobal.appointments.entity.Vac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VacRepository extends JpaRepository<Vac, UUID> {
    Optional<Vac> findByVacId(UUID vacId);
    
    Optional<Vac> findByVacCode(String vacCode);
}


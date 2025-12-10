package com.vfsglobal.bookings.repository;

import com.vfsglobal.bookings.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, UUID> {
    Optional<Applicant> findByPassportNumber(String passportNumber);
    Optional<Applicant> findByEmail(String email);
}


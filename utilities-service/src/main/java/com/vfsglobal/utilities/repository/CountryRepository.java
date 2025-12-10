package com.vfsglobal.utilities.repository;

import com.vfsglobal.utilities.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    Optional<Country> findByCountryId(UUID countryId);
    
    Optional<Country> findByCountryCode2(String countryCode2);
    
    @Query("SELECT c FROM Country c WHERE c.isActive = true ORDER BY c.displayOrder, c.countryName")
    List<Country> findAllActive();
    
    @Query("SELECT c FROM Country c WHERE c.isActive = true AND " +
           "(LOWER(c.countryName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.countryCode2) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.countryCode3) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY c.displayOrder, c.countryName")
    List<Country> searchActive(@Param("search") String search);
}


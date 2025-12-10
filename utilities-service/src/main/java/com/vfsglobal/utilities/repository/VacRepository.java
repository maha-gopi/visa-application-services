package com.vfsglobal.utilities.repository;

import com.vfsglobal.utilities.entity.Vac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VacRepository extends JpaRepository<Vac, UUID> {
    Optional<Vac> findByVacId(UUID vacId);
    
    Optional<Vac> findByVacCode(String vacCode);
    
    @Query("SELECT v FROM Vac v WHERE v.isActive = true")
    List<Vac> findAllActive();
    
    @Query("SELECT v FROM Vac v WHERE v.isActive = true AND v.country.countryCode2 = :country")
    List<Vac> findByCountry(@Param("country") String country);
    
    @Query("SELECT v FROM Vac v WHERE v.isActive = true AND v.country.countryId = :countryId")
    List<Vac> findByCountryId(@Param("countryId") UUID countryId);
    
    @Query("SELECT v FROM Vac v WHERE v.isActive = true AND v.city = :city")
    List<Vac> findByCity(@Param("city") String city);
    
    @Query("SELECT v FROM Vac v WHERE v.isActive = true AND " +
           "(LOWER(v.vacName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.address) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Vac> search(@Param("search") String search);
}


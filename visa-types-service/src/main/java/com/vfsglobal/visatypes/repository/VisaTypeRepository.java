package com.vfsglobal.visatypes.repository;

import com.vfsglobal.visatypes.entity.VisaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisaTypeRepository extends JpaRepository<VisaType, UUID> {

    @Query("SELECT vt FROM VisaType vt WHERE vt.isActive = :active")
    List<VisaType> findByActive(@Param("active") Boolean active);

    @Query("SELECT vt FROM VisaType vt WHERE vt.country.countryId = :countryId AND vt.isActive = :active")
    List<VisaType> findByCountryIdAndActive(@Param("countryId") UUID countryId, @Param("active") Boolean active);

    @Query("SELECT vt FROM VisaType vt WHERE vt.visaTypeCode = :code AND vt.isActive = true")
    Optional<VisaType> findByVisaTypeCode(@Param("code") String code);

    @Query("SELECT vt FROM VisaType vt WHERE vt.isActive = :active")
    Page<VisaType> findByActive(@Param("active") Boolean active, Pageable pageable);

    @Query("SELECT vt FROM VisaType vt WHERE vt.country.countryId = :countryId AND vt.isActive = :active")
    Page<VisaType> findByCountryIdAndActive(@Param("countryId") UUID countryId, @Param("active") Boolean active, Pageable pageable);

    @Query("SELECT vt FROM VisaType vt WHERE vt.country.countryCode2 = :countryCode AND vt.isActive = :active")
    List<VisaType> findByCountryCodeAndActive(@Param("countryCode") String countryCode, @Param("active") Boolean active);
}



package com.vfsglobal.bookings.repository;

import com.vfsglobal.bookings.entity.ServiceCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, UUID> {
    @Query("SELECT s FROM ServiceCatalog s WHERE s.serviceCode = :code AND s.isActive = true")
    Optional<ServiceCatalog> findByServiceCode(@Param("code") String code);

    @Query("SELECT s FROM ServiceCatalog s WHERE s.serviceCode IN :codes AND s.isActive = true")
    List<ServiceCatalog> findByServiceCodes(@Param("codes") List<String> codes);
}


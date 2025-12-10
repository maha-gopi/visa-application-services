package com.vfsglobal.utilities.repository;

import com.vfsglobal.utilities.entity.ApplicantPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicantPhotoRepository extends JpaRepository<ApplicantPhoto, UUID> {
    Optional<ApplicantPhoto> findByPhotoId(UUID photoId);
    
    List<ApplicantPhoto> findByApplicationId(UUID applicationId);
}


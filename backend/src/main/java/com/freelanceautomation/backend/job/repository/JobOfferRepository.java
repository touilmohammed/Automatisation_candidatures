package com.freelanceautomation.backend.job.repository;

import com.freelanceautomation.backend.job.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    Optional<JobOffer> findByPlatformAndExternalId(String platform, String externalId);

}
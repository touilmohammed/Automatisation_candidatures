package com.freelanceautomation.backend.job.service;

import com.freelanceautomation.backend.job.dto.JobWebhookRequest;
import com.freelanceautomation.backend.job.dto.JobWebhookResponse;
import com.freelanceautomation.backend.job.entity.JobOffer;
import com.freelanceautomation.backend.job.repository.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JobWebhookService {

    private final JobOfferRepository jobOfferRepository;

    public JobWebhookService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    public JobWebhookResponse ingest(JobWebhookRequest request) {
        var existing = jobOfferRepository.findByPlatformAndExternalId(
                request.platform().trim().toLowerCase(),
                request.externalId().trim()
        );

        if (existing.isPresent()) {
            return new JobWebhookResponse(existing.get().getId(), "DUPLICATE_IGNORED");
        }

        JobOffer jobOffer = JobOffer.builder()
                .externalId(request.externalId().trim())
                .platform(request.platform().trim().toLowerCase())
                .title(request.title().trim())
                .description(request.description().trim())
                .budget(request.budget())
                .currency(request.currency() == null ? null : request.currency().trim().toUpperCase())
                .location(request.location() == null ? null : request.location().trim())
                .remoteAllowed(request.remoteAllowed())
                .jobUrl(request.jobUrl().trim())
                .postedAt(request.postedAt())
                .ingestedAt(Instant.now())
                .build();

        JobOffer saved = jobOfferRepository.save(jobOffer);

        return new JobWebhookResponse(saved.getId(), "CREATED");
    }
}
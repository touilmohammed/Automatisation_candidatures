package com.freelanceautomation.backend.application.service;

import com.freelanceautomation.backend.ai.dto.AiGenerationResponse;
import com.freelanceautomation.backend.ai.service.AiGenerationService;
import com.freelanceautomation.backend.application.dto.ApplicationActionResponse;
import com.freelanceautomation.backend.application.dto.ApplicationResponse;
import com.freelanceautomation.backend.application.dto.ApplicationSubmitResponse;
import com.freelanceautomation.backend.application.entity.ApplicationRecord;
import com.freelanceautomation.backend.application.repository.ApplicationRecordRepository;
import com.freelanceautomation.backend.job.entity.JobOffer;
import com.freelanceautomation.backend.job.repository.JobOfferRepository;
import com.freelanceautomation.backend.user.entity.User;
import com.freelanceautomation.backend.user.repository.UserRepository;
import com.freelanceautomation.backend.notification.service.NotificationService;
import com.freelanceautomation.backend.audit.service.AuditService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ApplicationService {

    private final ApplicationRecordRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobOfferRepository jobRepository;
    private final AiGenerationService aiGenerationService;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public ApplicationService(
            ApplicationRecordRepository applicationRepository,
            UserRepository userRepository,
            JobOfferRepository jobRepository,
            AiGenerationService aiGenerationService,
            NotificationService notificationService,
            AuditService auditService
    ) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.aiGenerationService = aiGenerationService;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    public ApplicationResponse generateApplication(Long jobId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        JobOffer job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("JOB_NOT_FOUND"));

        AiGenerationResponse generation = aiGenerationService.generate(jobId, email);

        ApplicationRecord application = ApplicationRecord.builder()
                .user(user)
                .jobOffer(job)
                .generatedCv(generation.cvAdapted())
                .generatedCoverLetter(generation.coverLetter())
                .status("GENERATED")
                .createdAt(Instant.now())
                .build();

        ApplicationRecord saved = applicationRepository.save(application);
        
        notificationService.createNotification(
                email,
                "Application generated for job: " + job.getTitle()
        );

        auditService.logForUser(
                email,
                "APPLICATION_GENERATED",
                "Generated application for job: " + job.getTitle()
        );
        return toResponse(saved);
    }

    public ApplicationResponse getApplication(Long applicationId) {
        ApplicationRecord application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("APPLICATION_NOT_FOUND"));

        return toResponse(application);
    }

    public ApplicationActionResponse validateApplication(Long applicationId) {
        ApplicationRecord application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("APPLICATION_NOT_FOUND"));

        application.setStatus("VALIDATED");
        application.setReviewedAt(Instant.now());

        ApplicationRecord saved = applicationRepository.save(application);
        
        notificationService.createNotification(
                application.getUser().getEmail(),
                "Application validated for job: " + application.getJobOffer().getTitle()
        );

        auditService.logForUser(
                application.getUser().getEmail(),
                "APPLICATION_VALIDATED",
                "Validated application #" + application.getId() + " for job: " + application.getJobOffer().getTitle()
        );

        return new ApplicationActionResponse(saved.getId(), saved.getStatus());
    }

    public ApplicationActionResponse rejectApplication(Long applicationId) {
        ApplicationRecord application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("APPLICATION_NOT_FOUND"));

        application.setStatus("REJECTED");
        application.setReviewedAt(Instant.now());

        ApplicationRecord saved = applicationRepository.save(application);
        
        notificationService.createNotification(
                application.getUser().getEmail(),
                "Application rejected for job: " + application.getJobOffer().getTitle()
        );

        auditService.logForUser(
                application.getUser().getEmail(),
                "APPLICATION_REJECTED",
                "Rejected application #" + application.getId() + " for job: " + application.getJobOffer().getTitle()
        );

        return new ApplicationActionResponse(saved.getId(), saved.getStatus());
    }

    public ApplicationSubmitResponse submitApplication(Long applicationId) {
        ApplicationRecord application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("APPLICATION_NOT_FOUND"));

        if (!"VALIDATED".equals(application.getStatus())) {
            throw new IllegalArgumentException("APPLICATION_NOT_VALIDATED");
        }

        application.setStatus("SUBMITTED");
        application.setSubmissionMode("PLATFORM_REDIRECT");
        application.setSubmittedAt(Instant.now());

        ApplicationRecord saved = applicationRepository.save(application);

        notificationService.createNotification(
                application.getUser().getEmail(),
                "Application submitted for job: " + application.getJobOffer().getTitle()
        );

        auditService.logForUser(
                application.getUser().getEmail(),
                "APPLICATION_SUBMITTED",
                "Submitted application #" + application.getId() + " for job: " + application.getJobOffer().getTitle()
        );

        return new ApplicationSubmitResponse(
                saved.getId(),
                saved.getStatus(),
                saved.getSubmissionMode(),
                saved.getJobOffer().getJobUrl(),
                saved.getSubmittedAt()
        );
    }

    private ApplicationResponse toResponse(ApplicationRecord application) {
        return new ApplicationResponse(
                application.getId(),
                application.getJobOffer().getId(),
                application.getJobOffer().getTitle(),
                application.getGeneratedCv(),
                application.getGeneratedCoverLetter(),
                application.getStatus(),
                application.getCreatedAt(),
                application.getReviewedAt(),
                application.getSubmittedAt(),
                application.getSubmissionMode()
        );
    }
}
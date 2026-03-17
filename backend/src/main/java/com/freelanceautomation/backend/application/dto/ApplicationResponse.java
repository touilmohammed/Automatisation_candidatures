package com.freelanceautomation.backend.application.dto;

import java.time.Instant;

public record ApplicationResponse(
        Long id,
        Long jobId,
        String jobTitle,
        String generatedCv,
        String generatedCoverLetter,
        String status,
        Instant createdAt,
        Instant reviewedAt,
        Instant submittedAt,
        String submissionMode
) {
}
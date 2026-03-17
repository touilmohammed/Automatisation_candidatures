package com.freelanceautomation.backend.application.dto;

import java.time.Instant;

public record ApplicationSubmitResponse(
        Long applicationId,
        String status,
        String submissionMode,
        String targetUrl,
        Instant submittedAt
) {
}
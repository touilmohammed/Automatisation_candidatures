package com.freelanceautomation.backend.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record JobWebhookRequest(
        @NotBlank String externalId,
        @NotBlank String platform,
        @NotBlank String title,
        @NotBlank String description,
        BigDecimal budget,
        String currency,
        String location,
        @NotNull Boolean remoteAllowed,
        @NotBlank String jobUrl,
        Instant postedAt
) {
}
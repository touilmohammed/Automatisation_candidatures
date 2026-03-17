package com.freelanceautomation.backend.audit.dto;

import java.time.Instant;

public record AuditLogResponse(
        Long id,
        String action,
        String details,
        Instant createdAt
) {
}
package com.freelanceautomation.backend.notification.dto;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String message,
        Boolean isRead,
        Instant createdAt,
        Instant readAt
) {
}
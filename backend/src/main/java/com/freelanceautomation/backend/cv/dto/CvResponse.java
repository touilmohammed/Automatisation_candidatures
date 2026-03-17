package com.freelanceautomation.backend.cv.dto;

import java.time.Instant;

public record CvResponse(
        Long id,
        String filename,
        String mimeType,
        Instant uploadedAt
) {}
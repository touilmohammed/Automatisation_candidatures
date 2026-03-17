package com.freelanceautomation.backend.cv.dto;

public record CvTextResponse(
        String filename,
        String extractedText,
        Integer textLength
) {
}
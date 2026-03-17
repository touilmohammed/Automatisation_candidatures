package com.freelanceautomation.backend.matching.dto;

import java.util.List;

public record MatchingContext(
        String profileTitle,
        List<String> profileSkills,
        String cvText,
        String jobText
) {
}
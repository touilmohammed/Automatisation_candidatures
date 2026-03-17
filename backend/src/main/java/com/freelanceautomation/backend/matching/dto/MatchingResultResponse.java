package com.freelanceautomation.backend.matching.dto;

import java.util.List;

public record MatchingResultResponse(
        Long jobId,
        int score,
        List<String> matchedKeywords,
        String decision
) {}
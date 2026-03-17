package com.freelanceautomation.backend.matching.controller;

import com.freelanceautomation.backend.matching.dto.MatchingResultResponse;
import com.freelanceautomation.backend.matching.service.MatchingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping("/jobs/{jobId}/evaluate")
    public MatchingResultResponse evaluate(
            @PathVariable Long jobId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return matchingService.evaluate(jobId, email);
    }
}
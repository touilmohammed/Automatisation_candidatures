package com.freelanceautomation.backend.ai.controller;

import com.freelanceautomation.backend.ai.dto.AiGenerationResponse;
import com.freelanceautomation.backend.ai.service.AiGenerationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiGenerationController {

    private final AiGenerationService aiService;

    public AiGenerationController(AiGenerationService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/jobs/{jobId}/generate")
    public AiGenerationResponse generate(
            @PathVariable Long jobId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return aiService.generate(jobId, email);
    }
}
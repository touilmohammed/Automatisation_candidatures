package com.freelanceautomation.backend.application.controller;

import com.freelanceautomation.backend.application.dto.ApplicationActionResponse;
import com.freelanceautomation.backend.application.dto.ApplicationResponse;
import com.freelanceautomation.backend.application.service.ApplicationService;
import com.freelanceautomation.backend.application.dto.ApplicationSubmitResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/jobs/{jobId}/generate")
    public ApplicationResponse generate(@PathVariable Long jobId, Authentication authentication) {
        return applicationService.generateApplication(jobId, authentication.getName());
    }

    @GetMapping("/{applicationId}")
    public ApplicationResponse getOne(@PathVariable Long applicationId) {
        return applicationService.getApplication(applicationId);
    }

    @PostMapping("/{applicationId}/validate")
    public ApplicationActionResponse validate(@PathVariable Long applicationId) {
        return applicationService.validateApplication(applicationId);
    }

    @PostMapping("/{applicationId}/reject")
    public ApplicationActionResponse reject(@PathVariable Long applicationId) {
        return applicationService.rejectApplication(applicationId);
    }

    @PostMapping("/{applicationId}/submit")
    public ApplicationSubmitResponse submit(@PathVariable Long applicationId) {
        return applicationService.submitApplication(applicationId);
    }
}


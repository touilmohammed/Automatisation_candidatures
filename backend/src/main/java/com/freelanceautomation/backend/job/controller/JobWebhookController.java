package com.freelanceautomation.backend.job.controller;

import com.freelanceautomation.backend.job.dto.JobWebhookRequest;
import com.freelanceautomation.backend.job.dto.JobWebhookResponse;
import com.freelanceautomation.backend.job.service.JobWebhookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks/jobs")
public class JobWebhookController {

    private final JobWebhookService jobWebhookService;

    public JobWebhookController(JobWebhookService jobWebhookService) {
        this.jobWebhookService = jobWebhookService;
    }

    @PostMapping
    public ResponseEntity<JobWebhookResponse> ingest(@Valid @RequestBody JobWebhookRequest request) {
        return ResponseEntity.ok(jobWebhookService.ingest(request));
    }
}
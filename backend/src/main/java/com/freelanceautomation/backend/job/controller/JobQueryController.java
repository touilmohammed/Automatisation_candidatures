package com.freelanceautomation.backend.job.controller;

import com.freelanceautomation.backend.job.entity.JobOffer;
import com.freelanceautomation.backend.job.repository.JobOfferRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobQueryController {

    private final JobOfferRepository jobOfferRepository;

    public JobQueryController(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    @GetMapping("/api/jobs")
    public List<JobOffer> getAllJobs() {
        return jobOfferRepository.findAll();
    }
}
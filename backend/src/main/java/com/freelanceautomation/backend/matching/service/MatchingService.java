package com.freelanceautomation.backend.matching.service;

import com.freelanceautomation.backend.cv.entity.CvDocument;
import com.freelanceautomation.backend.cv.repository.CvDocumentRepository;
import com.freelanceautomation.backend.job.entity.JobOffer;
import com.freelanceautomation.backend.job.repository.JobOfferRepository;
import com.freelanceautomation.backend.matching.dto.MatchingContext;
import com.freelanceautomation.backend.matching.dto.MatchingResultResponse;
import com.freelanceautomation.backend.matching.scoring.KeywordScoringEngine;
import com.freelanceautomation.backend.profile.entity.Profile;
import com.freelanceautomation.backend.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MatchingService {

    private final CvDocumentRepository cvRepository;
    private final JobOfferRepository jobRepository;
    private final ProfileRepository profileRepository;
    private final KeywordScoringEngine scoringEngine;

    public MatchingService(
            CvDocumentRepository cvRepository,
            JobOfferRepository jobRepository,
            ProfileRepository profileRepository,
            KeywordScoringEngine scoringEngine
    ) {
        this.cvRepository = cvRepository;
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
        this.scoringEngine = scoringEngine;
    }

    public MatchingResultResponse evaluate(Long jobId, String userEmail) {

        CvDocument cv = cvRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("CV_NOT_FOUND"));

        Profile profile = profileRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("PROFILE_NOT_FOUND"));

        JobOffer job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("JOB_NOT_FOUND"));

        List<String> profileSkills = parseSkills(profile.getSkills());

        MatchingContext context = new MatchingContext(
                profile.getTitle(),
                profileSkills,
                cv.getExtractedText(),
                job.getTitle() + "\n" + job.getDescription()
        );

        var result = scoringEngine.score(context);

        int score = result.score();
        String decision = score >= 40 ? "ACCEPTED" : "REJECTED";

        return new MatchingResultResponse(
                jobId,
                score,
                result.matchedKeywords(),
                decision
        );
    }

    private List<String> parseSkills(String rawSkills) {
        if (rawSkills == null || rawSkills.isBlank()) {
            return List.of();
        }

        return Arrays.stream(rawSkills.split(","))
                .map(String::trim)
                .filter(skill -> !skill.isBlank())
                .toList();
    }
}
package com.freelanceautomation.backend.ai.service;

import com.freelanceautomation.backend.ai.dto.AiGenerationResponse;
import com.freelanceautomation.backend.cv.entity.CvDocument;
import com.freelanceautomation.backend.cv.repository.CvDocumentRepository;
import com.freelanceautomation.backend.job.entity.JobOffer;
import com.freelanceautomation.backend.job.repository.JobOfferRepository;
import com.freelanceautomation.backend.profile.entity.Profile;
import com.freelanceautomation.backend.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class AiGenerationService {

    private final CvDocumentRepository cvRepository;
    private final JobOfferRepository jobRepository;
    private final ProfileRepository profileRepository;
    private final MistralClientService mistralClientService;

    public AiGenerationService(
            CvDocumentRepository cvRepository,
            JobOfferRepository jobRepository,
            ProfileRepository profileRepository,
            MistralClientService mistralClientService
    ) {
        this.cvRepository = cvRepository;
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
        this.mistralClientService = mistralClientService;
    }

    public AiGenerationResponse generate(Long jobId, String email) {
        CvDocument cv = cvRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("CV_NOT_FOUND"));

        Profile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("PROFILE_NOT_FOUND"));

        JobOffer job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("JOB_NOT_FOUND"));

        String cvText = cv.getExtractedText() == null ? "" : cv.getExtractedText();

        String systemPrompt = """
                You are an assistant helping a freelance improve job applications.
                You must NEVER invent skills, experiences, certifications, companies, or achievements.
                You must only rephrase, reorganize, or highlight information already present.
                If some information is missing, say it explicitly.
                Return professional output in French.
                """;

        String cvPrompt = """
                Adapt this CV to the following freelance job offer.

                STRICT RULES:
                - Do NOT add new skills
                - Do NOT add new experiences
                - Do NOT modify dates or companies
                - You may reorder sections and rephrase bullet points
                - Stay faithful to the source CV

                PROFILE TITLE:
                %s

                PROFILE SKILLS:
                %s

                ORIGINAL CV:
                %s

                JOB OFFER TITLE:
                %s

                JOB OFFER DESCRIPTION:
                %s

                Return only the adapted CV text in French.
                """.formatted(
                profile.getTitle(),
                profile.getSkills(),
                cvText,
                job.getTitle(),
                job.getDescription()
        );

        String letterPrompt = """
                Write a professional freelance cover letter in French.

                CONSTRAINTS:
                - Use only information from the provided profile and CV
                - Do not invent achievements or skills
                - Be concise and professional
                - Tone: confident, honest, human
                - Length: 200 to 300 words

                PROFILE TITLE:
                %s

                PROFILE SKILLS:
                %s

                ORIGINAL CV:
                %s

                JOB OFFER TITLE:
                %s

                JOB OFFER DESCRIPTION:
                %s

                Return only the cover letter text in French.
                """.formatted(
                profile.getTitle(),
                profile.getSkills(),
                cvText,
                job.getTitle(),
                job.getDescription()
        );

        String cvAdapted = mistralClientService.generateText(systemPrompt, cvPrompt);
        String coverLetter = mistralClientService.generateText(systemPrompt, letterPrompt);

        return new AiGenerationResponse(cvAdapted, coverLetter);
    }
}
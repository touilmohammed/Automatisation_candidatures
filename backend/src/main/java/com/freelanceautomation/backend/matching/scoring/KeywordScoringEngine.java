package com.freelanceautomation.backend.matching.scoring;

import com.freelanceautomation.backend.matching.dto.MatchingContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class KeywordScoringEngine {

    public Result score(MatchingContext context) {
        String profileTitle = safe(context.profileTitle());
        String cvText = safe(context.cvText());
        String jobText = safe(context.jobText());

        List<String> normalizedSkills = context.profileSkills().stream()
                .map(this::normalize)
                .filter(skill -> !skill.isBlank())
                .distinct()
                .toList();

        int score = 0;
        Set<String> matchedKeywords = new LinkedHashSet<>();

        for (String skill : normalizedSkills) {
            boolean inJob = containsTerm(jobText, skill);
            boolean inCv = containsTerm(cvText, skill);

            if (inJob && inCv) {
                score += 15;
                matchedKeywords.add(skill);
            } else if (inJob) {
                score += 10;
                matchedKeywords.add(skill);
            }
        }

        List<String> titleTokens = extractMeaningfulTokens(profileTitle);
        for (String token : titleTokens) {
            if (containsTerm(jobText, token)) {
                score += 5;
                matchedKeywords.add(token);
            }
        }

        if (jobText.contains("remote") && cvText.contains("remote")) {
            score += 5;
            matchedKeywords.add("remote");
        }

        score = Math.min(score, 100);

        return new Result(score, new ArrayList<>(matchedKeywords));
    }

    private String safe(String value) {
        return normalize(value == null ? "" : value);
    }

    private String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).trim();
    }

    private boolean containsTerm(String text, String term) {
        if (term.isBlank()) {
            return false;
        }
        return text.contains(term);
    }

    private List<String> extractMeaningfulTokens(String title) {
        String[] rawTokens = title.split("[\\s,/()\\-]+");
        List<String> tokens = new ArrayList<>();

        for (String token : rawTokens) {
            String normalized = normalize(token);
            if (normalized.length() >= 4) {
                tokens.add(normalized);
            }
        }

        return tokens;
    }

    public record Result(int score, List<String> matchedKeywords) {}
}
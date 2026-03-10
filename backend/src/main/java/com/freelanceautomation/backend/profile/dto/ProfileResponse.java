package com.freelanceautomation.backend.profile.dto;

import java.math.BigDecimal;

public record ProfileResponse(
        Long id,
        String email,
        String title,
        String skills,
        Integer yearsOfExperience,
        BigDecimal dailyRate,
        String missionPreferences
) {
}
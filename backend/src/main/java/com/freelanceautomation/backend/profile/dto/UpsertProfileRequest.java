package com.freelanceautomation.backend.profile.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpsertProfileRequest(
        @NotBlank String title,
        @NotBlank String skills,
        @NotNull @Min(0) Integer yearsOfExperience,
        @NotNull @DecimalMin("0.0") BigDecimal dailyRate,
        @NotBlank String missionPreferences
) {
}
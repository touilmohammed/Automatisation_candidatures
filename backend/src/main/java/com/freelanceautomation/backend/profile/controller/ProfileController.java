package com.freelanceautomation.backend.profile.controller;

import com.freelanceautomation.backend.profile.dto.ProfileResponse;
import com.freelanceautomation.backend.profile.dto.UpsertProfileRequest;
import com.freelanceautomation.backend.profile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ProfileResponse getMyProfile(Authentication authentication) {
        return profileService.getMyProfile(authentication.getName());
    }

    @PutMapping("/me")
    public ProfileResponse upsertMyProfile(@Valid @RequestBody UpsertProfileRequest request,
                                           Authentication authentication) {
        return profileService.upsertMyProfile(authentication.getName(), request);
    }
}
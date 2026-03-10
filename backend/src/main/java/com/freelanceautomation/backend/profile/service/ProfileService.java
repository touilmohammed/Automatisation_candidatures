package com.freelanceautomation.backend.profile.service;

import com.freelanceautomation.backend.profile.dto.ProfileResponse;
import com.freelanceautomation.backend.profile.dto.UpsertProfileRequest;
import com.freelanceautomation.backend.profile.entity.Profile;
import com.freelanceautomation.backend.profile.repository.ProfileRepository;
import com.freelanceautomation.backend.user.entity.User;
import com.freelanceautomation.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProfileResponse getMyProfile(String email) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("PROFILE_NOT_FOUND"));

        return toResponse(profile);
    }

    public ProfileResponse upsertMyProfile(String email, UpsertProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Profile profile = profileRepository.findByUserEmail(email)
                .orElse(Profile.builder().user(user).build());

        profile.setTitle(request.title().trim());
        profile.setSkills(request.skills().trim());
        profile.setYearsOfExperience(request.yearsOfExperience());
        profile.setDailyRate(request.dailyRate());
        profile.setMissionPreferences(request.missionPreferences().trim());

        Profile savedProfile = profileRepository.save(profile);
        return toResponse(savedProfile);
    }

    private ProfileResponse toResponse(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getUser().getEmail(),
                profile.getTitle(),
                profile.getSkills(),
                profile.getYearsOfExperience(),
                profile.getDailyRate(),
                profile.getMissionPreferences()
        );
    }
}
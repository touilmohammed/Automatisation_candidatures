package com.freelanceautomation.backend.ai.dto;

import java.util.List;
import java.util.Map;

public record MistralChatRequest(
        String model,
        List<Map<String, Object>> messages,
        double temperature
) {
}
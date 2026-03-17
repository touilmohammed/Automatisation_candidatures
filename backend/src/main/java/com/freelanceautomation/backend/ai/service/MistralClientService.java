package com.freelanceautomation.backend.ai.service;

import com.freelanceautomation.backend.ai.dto.MistralChatRequest;
import com.freelanceautomation.backend.ai.dto.MistralChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class MistralClientService {

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public MistralClientService(
            RestClient mistralRestClient,
            @Value("${app.mistral.api-key}") String apiKey,
            @Value("${app.mistral.model}") String model
    ) {
        this.restClient = mistralRestClient;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String generateText(String systemPrompt, String userPrompt) {
        MistralChatRequest request = new MistralChatRequest(
                model,
                List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                0.2
        );

        MistralChatResponse response = restClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(MistralChatResponse.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new IllegalStateException("EMPTY_MISTRAL_RESPONSE");
        }

        String content = response.choices().get(0).message().content();
        if (content == null || content.isBlank()) {
            throw new IllegalStateException("EMPTY_MISTRAL_CONTENT");
        }

        return content;
    }
}
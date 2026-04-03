package com.example.aichatbot.service.impl;

import com.example.aichatbot.dto.gemini.GeminiRequest;
import com.example.aichatbot.dto.gemini.GeminiResponse;
import com.example.aichatbot.entity.ChatMessage;
import com.example.aichatbot.entity.MessageRole;
import com.example.aichatbot.exception.GeminiApiException;
import com.example.aichatbot.service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GeminiServiceImpl implements GeminiService {

    private final WebClient geminiWebClient;
    private final String apiKey;
    private final String model;

    public GeminiServiceImpl(
            WebClient geminiWebClient,
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.model}") String model) {
        this.geminiWebClient = geminiWebClient;
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public String generateResponse(String userMessage, List<ChatMessage> history, String requestedModel) {
        GeminiRequest request = buildRequest(userMessage, history);

        String selectedModel = (requestedModel != null && !requestedModel.isBlank()) ? requestedModel : model;
        log.debug("Calling Gemini API with model: {}", selectedModel);

        GeminiResponse response = geminiWebClient.post()
                .uri("/models/{model}:generateContent?key={key}", selectedModel, apiKey)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(
                                new GeminiApiException("Client error from Gemini API: " + body))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(
                                new GeminiApiException("Server error from Gemini API: " + body))))
                .bodyToMono(GeminiResponse.class)
                .block();

        return extractText(response);
    }

    private GeminiRequest buildRequest(String userMessage, List<ChatMessage> history) {
        List<GeminiRequest.Content> contents = new ArrayList<>();

        for (ChatMessage msg : history) {
            String role = msg.getRole() == MessageRole.USER ? "user" : "model";
            contents.add(new GeminiRequest.Content(role, List.of(new GeminiRequest.Part(msg.getContent()))));
        }

        contents.add(new GeminiRequest.Content("user", List.of(new GeminiRequest.Part(userMessage))));

        return new GeminiRequest(contents);
    }

    private String extractText(GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            throw new GeminiApiException("Empty response from Gemini API");
        }

        GeminiResponse.Candidate candidate = response.candidates().getFirst();
        if (candidate.content() == null || candidate.content().parts() == null
                || candidate.content().parts().isEmpty()) {
            throw new GeminiApiException("No content in Gemini API response");
        }

        return candidate.content().parts().stream()
                .map(GeminiResponse.Part::text)
                .reduce((a, b) -> a + b)
                .orElseThrow(() -> new GeminiApiException("Failed to extract text from Gemini response"));
    }
}

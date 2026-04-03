package com.example.aichatbot.dto.gemini;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {

    public record Candidate(Content content) {
    }

    public record Content(List<Part> parts, String role) {
    }

    public record Part(String text) {
    }
}

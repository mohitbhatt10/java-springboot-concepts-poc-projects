package com.example.aichatbot.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatSessionResponse(
        UUID id,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

package com.example.aichatbot.dto;

import com.example.aichatbot.entity.MessageRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        MessageRole role,
        String content,
        LocalDateTime createdAt) {
}

package com.example.aichatbot.dto;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(
                @NotBlank(message = "Message must not be blank") String message,
                String model) {
}

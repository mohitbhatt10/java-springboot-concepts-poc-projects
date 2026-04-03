package com.example.aichatbot.service;

import com.example.aichatbot.entity.ChatMessage;

import java.util.List;

public interface GeminiService {
    String generateResponse(String userMessage, List<ChatMessage> history, String model);
}

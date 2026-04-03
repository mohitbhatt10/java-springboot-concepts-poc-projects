package com.example.aichatbot.service;

import com.example.aichatbot.dto.*;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatSessionResponse createSession(CreateSessionRequest request);

    List<ChatSessionResponse> getAllSessions();

    ChatMessageResponse sendMessage(UUID sessionId, SendMessageRequest request);

    List<ChatMessageResponse> getChatHistory(UUID sessionId);
}

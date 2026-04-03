package com.example.aichatbot.service.impl;

import com.example.aichatbot.dto.*;
import com.example.aichatbot.entity.ChatMessage;
import com.example.aichatbot.entity.ChatSession;
import com.example.aichatbot.entity.MessageRole;
import com.example.aichatbot.exception.ResourceNotFoundException;
import com.example.aichatbot.entity.AppUser;
import com.example.aichatbot.repository.AppUserRepository;
import com.example.aichatbot.repository.ChatMessageRepository;
import com.example.aichatbot.repository.ChatSessionRepository;
import com.example.aichatbot.service.ChatService;
import com.example.aichatbot.service.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final AppUserRepository userRepository;
    private final GeminiService geminiService;

    private AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Override
    @Transactional
    public ChatSessionResponse createSession(CreateSessionRequest request) {
        AppUser user = getCurrentUser();
        ChatSession session = ChatSession.builder()
                .title(request.title() != null ? request.title() : "New Chat")
                .user(user)
                .build();
        ChatSession saved = sessionRepository.save(session);
        log.info("Created chat session: {}", saved.getId());
        return toSessionResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatSessionResponse> getAllSessions() {
        AppUser user = getCurrentUser();
        return sessionRepository.findByUserIdOrderByUpdatedAtDesc(user.getId()).stream()
                .map(this::toSessionResponse)
                .toList();
    }

    @Override
    @Transactional
    public ChatMessageResponse sendMessage(UUID sessionId, SendMessageRequest request) {
        AppUser user = getCurrentUser();
        ChatSession session = sessionRepository.findByIdAndUserId(sessionId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found: " + sessionId));

        // Save user message
        ChatMessage userMessage = ChatMessage.builder()
                .session(session)
                .role(MessageRole.USER)
                .content(request.message())
                .build();
        messageRepository.save(userMessage);

        // Auto-generate session title from first message
        List<ChatMessage> history = messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        if (history.size() == 1) {
            String title = request.message().length() > 50
                    ? request.message().substring(0, 50) + "..."
                    : request.message();
            session.setTitle(title);
        }

        // Call Gemini API with chat history
        log.debug("Sending message to Gemini for session: {}", sessionId);
        String aiResponse = geminiService.generateResponse(request.message(), history, request.model());

        // Save AI response
        ChatMessage aiMessage = ChatMessage.builder()
                .session(session)
                .role(MessageRole.ASSISTANT)
                .content(aiResponse)
                .build();
        ChatMessage savedAiMessage = messageRepository.save(aiMessage);

        // Update session timestamp
        sessionRepository.save(session);

        log.info("AI response saved for session: {}", sessionId);
        return toMessageResponse(savedAiMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(UUID sessionId) {
        AppUser user = getCurrentUser();
        if (!sessionRepository.existsByIdAndUserId(sessionId, user.getId())) {
            throw new ResourceNotFoundException("Chat session not found: " + sessionId);
        }
        return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(this::toMessageResponse)
                .toList();
    }

    private ChatSessionResponse toSessionResponse(ChatSession session) {
        return new ChatSessionResponse(
                session.getId(),
                session.getTitle(),
                session.getCreatedAt(),
                session.getUpdatedAt());
    }

    private ChatMessageResponse toMessageResponse(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getRole(),
                message.getContent(),
                message.getCreatedAt());
    }
}

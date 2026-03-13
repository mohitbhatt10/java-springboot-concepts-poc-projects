package com.example.soildprincipalsdemo.service.impl;

import com.example.soildprincipalsdemo.model.DipRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;
import com.example.soildprincipalsdemo.service.DipDemoService;
import com.example.soildprincipalsdemo.solid.dip.MessageSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DIP demonstration service implementation.
 *
 * High-level service depends on MessageSender abstraction.
 */
@Service
public class DipDemoServiceImpl implements DipDemoService {

    private final Map<String, MessageSender> senderMap;

    public DipDemoServiceImpl(List<MessageSender> senders) {
        // Step 1: Build channel->sender map once at startup.
        this.senderMap = senders.stream()
                .collect(Collectors.toMap(s -> s.channel().toUpperCase(), Function.identity()));
    }

    @Override
    public PrincipleDemoResponse demonstrate(DipRequest request) {
        // Step 2: Normalize requested channel.
        String channel = request.getChannel().toUpperCase();

        // Step 3: Resolve concrete sender through abstraction map.
        MessageSender sender = senderMap.get(channel);
        if (sender == null) {
            throw new IllegalArgumentException("Unsupported channel: " + request.getChannel());
        }

        // Step 4: Send message via abstraction.
        String deliveryTrace = sender.send(request.getRecipient(), request.getMessage());

        // Step 5: Return explanatory response.
        return new PrincipleDemoResponse("DIP", "PASS",
                "High-level module depends on abstraction, not concrete sender class.")
                .addDetail("channel", channel)
                .addDetail("senderUsed", sender.getClass().getSimpleName())
                .addDetail("deliveryTrace", deliveryTrace);
    }
}

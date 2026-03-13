package com.example.soildprincipalsdemo.solid.dip;

import org.springframework.stereotype.Component;

/**
 * Email sender implementation of MessageSender abstraction.
 */
@Component
public class EmailMessageSender implements MessageSender {

    @Override
    public String channel() {
        return "EMAIL";
    }

    @Override
    public String send(String recipient, String message) {
        return "Email sent to " + recipient + " with message: " + message;
    }
}

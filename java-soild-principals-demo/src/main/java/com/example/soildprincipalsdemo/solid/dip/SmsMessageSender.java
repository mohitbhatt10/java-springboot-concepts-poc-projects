package com.example.soildprincipalsdemo.solid.dip;

import org.springframework.stereotype.Component;

/**
 * SMS sender implementation of MessageSender abstraction.
 */
@Component
public class SmsMessageSender implements MessageSender {

    @Override
    public String channel() {
        return "SMS";
    }

    @Override
    public String send(String recipient, String message) {
        return "SMS sent to " + recipient + " with message: " + message;
    }
}

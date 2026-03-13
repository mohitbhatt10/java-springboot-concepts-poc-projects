package com.example.soildprincipalsdemo.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for the DIP endpoint.
 */
public class DipRequest {

    /**
     * Delivery channel to pick a concrete sender behind an abstraction.
     * Allowed values in this demo: EMAIL, SMS.
     */
    @NotBlank(message = "channel is required")
    private String channel;

    /** Recipient target value (email address or phone number in this demo). */
    @NotBlank(message = "recipient is required")
    private String recipient;

    /** Message content that should be sent. */
    @NotBlank(message = "message is required")
    private String message;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

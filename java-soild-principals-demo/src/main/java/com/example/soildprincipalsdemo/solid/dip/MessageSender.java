package com.example.soildprincipalsdemo.solid.dip;

/**
 * Abstraction for message delivery used by DIP demonstration.
 *
 * High-level modules depend on this interface, not on concrete sender classes.
 */
public interface MessageSender {

    /**
     * @return channel key used for sender resolution.
     */
    String channel();

    /**
     * Sends a message through concrete channel and returns a trace message.
     */
    String send(String recipient, String message);
}

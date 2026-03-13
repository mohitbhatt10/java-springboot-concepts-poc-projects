package com.example.soildprincipalsdemo.solid.srp;

import org.springframework.stereotype.Component;

/**
 * Notifier class for SRP demo.
 *
 * Single responsibility: send notification information.
 */
@Component
public class OrderNotifier {

    /**
     * Simulates email notification and returns a trace message.
     */
    public String notifyCustomer(OrderData orderData) {
        return "Notification sent to " + orderData.getCustomerEmail() + " for order " + orderData.getOrderId() + ".";
    }
}

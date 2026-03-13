package com.example.soildprincipalsdemo.solid.srp;

import org.springframework.stereotype.Component;

/**
 * Repository simulator class for SRP demo.
 *
 * Single responsibility: represent persistence action (simulated, in-memory
 * style).
 */
@Component
public class OrderRepositorySimulator {

    /**
     * Simulates save operation and returns a trace message.
     */
    public String save(OrderData orderData) {
        return "Order " + orderData.getOrderId() + " persisted successfully.";
    }
}

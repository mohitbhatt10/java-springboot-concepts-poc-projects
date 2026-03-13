package com.example.soildprincipalsdemo.solid.srp;

import org.springframework.stereotype.Component;

/**
 * Validator class for SRP demo.
 *
 * Single responsibility: validate business constraints for an order.
 */
@Component
public class OrderValidator {

    /**
     * Validates basic order constraints.
     */
    public void validate(OrderData orderData) {
        if (orderData.getOrderId() == null || orderData.getOrderId().isBlank()) {
            throw new IllegalArgumentException("Order ID must not be blank");
        }
        if (orderData.getCustomerEmail() == null || orderData.getCustomerEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email must not be blank");
        }
        if (orderData.getAmount() <= 0) {
            throw new IllegalArgumentException("Order amount must be greater than zero");
        }
    }
}

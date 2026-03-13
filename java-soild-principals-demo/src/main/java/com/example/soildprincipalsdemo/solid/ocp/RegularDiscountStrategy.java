package com.example.soildprincipalsdemo.solid.ocp;

import org.springframework.stereotype.Component;

/**
 * Discount strategy for REGULAR customers.
 *
 * A tiny discount is applied to represent standard customer benefits.
 */
@Component
public class RegularDiscountStrategy implements DiscountStrategy {

    @Override
    public String getType() {
        return "REGULAR";
    }

    @Override
    public double apply(double amount) {
        // Step 1: Define the discount percentage for regular users.
        double discountRate = 0.05;

        // Step 2: Apply discount by subtracting percentage from the original amount.
        return amount - (amount * discountRate);
    }
}

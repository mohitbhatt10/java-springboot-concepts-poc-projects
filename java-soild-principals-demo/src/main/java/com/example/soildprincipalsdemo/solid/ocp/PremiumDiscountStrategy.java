package com.example.soildprincipalsdemo.solid.ocp;

import org.springframework.stereotype.Component;

/**
 * Discount strategy for PREMIUM customers.
 *
 * This class demonstrates extending behavior without changing core calculator
 * code.
 */
@Component
public class PremiumDiscountStrategy implements DiscountStrategy {

    @Override
    public String getType() {
        return "PREMIUM";
    }

    @Override
    public double apply(double amount) {
        // Step 1: Define a higher discount percentage for premium users.
        double discountRate = 0.15;

        // Step 2: Apply discount and return final amount.
        return amount - (amount * discountRate);
    }
}

package com.example.soildprincipalsdemo.solid.ocp;

/**
 * Strategy abstraction for Open/Closed Principle demonstration.
 *
 * The calculator depends on this abstraction and remains closed for
 * modification.
 * New discount behavior can be added by creating a new implementation class.
 */
public interface DiscountStrategy {

    /**
     * @return strategy key used to route incoming requests to this implementation.
     */
    String getType();

    /**
     * Applies discount rules and returns the final amount.
     */
    double apply(double amount);
}

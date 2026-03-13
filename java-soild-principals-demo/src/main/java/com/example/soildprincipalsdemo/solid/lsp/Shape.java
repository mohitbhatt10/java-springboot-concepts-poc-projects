package com.example.soildprincipalsdemo.solid.lsp;

/**
 * Abstraction used for Liskov Substitution Principle demonstration.
 *
 * Any concrete shape implementing this interface should be safely substitutable
 * wherever this abstraction is expected.
 */
public interface Shape {

    /**
     * Calculates geometric area.
     */
    double area();

    /**
     * @return readable shape type for response explanation.
     */
    String type();
}

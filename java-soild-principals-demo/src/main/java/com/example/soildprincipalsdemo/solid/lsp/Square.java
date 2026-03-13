package com.example.soildprincipalsdemo.solid.lsp;

/**
 * Square implementation of Shape.
 *
 * We model square independently via the Shape abstraction to avoid fragile
 * inheritance hierarchies and keep substitution behavior predictable.
 */
public class Square implements Shape {

    private final double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public double area() {
        // Square area formula: side^2.
        return side * side;
    }

    @Override
    public String type() {
        return "SQUARE";
    }
}

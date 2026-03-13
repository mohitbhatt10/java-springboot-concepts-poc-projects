package com.example.soildprincipalsdemo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request payload for the LSP endpoint.
 */
public class LspRequest {

    /**
     * Shape type that will be substituted through the same abstraction.
     * Allowed values in this demo: RECTANGLE, SQUARE.
     */
    @NotBlank(message = "shape is required")
    private String shape;

    /** Width input for selected shape. */
    @NotNull(message = "width is required")
    @Positive(message = "width must be greater than zero")
    private Double width;

    /** Height input for selected shape. Used by rectangle, ignored by square. */
    @NotNull(message = "height is required")
    @Positive(message = "height must be greater than zero")
    private Double height;

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}

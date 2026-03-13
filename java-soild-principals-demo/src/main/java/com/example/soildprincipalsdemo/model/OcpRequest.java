package com.example.soildprincipalsdemo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request payload for the OCP endpoint.
 */
public class OcpRequest {

    /**
     * Customer type determines which strategy implementation is selected.
     * Allowed values in this demo: REGULAR, PREMIUM.
     */
    @NotBlank(message = "customerType is required")
    private String customerType;

    /**
     * Input amount that discount strategy will process.
     */
    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than zero")
    private Double amount;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

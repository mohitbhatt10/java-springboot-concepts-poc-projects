package com.example.soildprincipalsdemo.solid.srp;

/**
 * Simple model used in SRP demonstration.
 *
 * This model only stores data and has no business logic.
 */
public class OrderData {

    private String orderId;
    private String customerEmail;
    private double amount;

    public OrderData(String orderId, String customerEmail, double amount) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

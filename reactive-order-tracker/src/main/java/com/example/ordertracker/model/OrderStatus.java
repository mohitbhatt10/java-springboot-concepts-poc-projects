package com.example.ordertracker.model;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PACKED,
    OUT_FOR_DELIVERY,
    DELIVERED;

    public boolean isTerminal() {
        return this == DELIVERED;
    }
}

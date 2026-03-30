package com.example.j21demo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PhysicalOrder(@Valid Customer customer, @Valid DeliveryAddress shippingAddress,
        @NotEmpty List<String> items) implements DemoOrder {
}
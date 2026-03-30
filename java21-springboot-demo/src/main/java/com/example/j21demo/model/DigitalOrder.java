package com.example.j21demo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DigitalOrder(@Valid Customer customer, @Email String downloadEmail,
        @NotEmpty List<String> entitlements) implements DemoOrder {
}
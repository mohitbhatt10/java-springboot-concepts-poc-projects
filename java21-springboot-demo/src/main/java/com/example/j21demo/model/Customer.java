package com.example.j21demo.model;

import jakarta.validation.constraints.NotBlank;

public record Customer(@NotBlank String customerId, @NotBlank String tier) {
}
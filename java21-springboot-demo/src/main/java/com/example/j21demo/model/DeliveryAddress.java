package com.example.j21demo.model;

import jakarta.validation.constraints.NotBlank;

public record DeliveryAddress(@NotBlank String city, @NotBlank String country) {
}
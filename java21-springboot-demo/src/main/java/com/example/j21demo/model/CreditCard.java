package com.example.j21demo.model;

import jakarta.validation.constraints.NotBlank;

public record CreditCard(@NotBlank String number, boolean corporateCard) implements PaymentMethod {
}

package com.example.j21demo.model;

import jakarta.validation.constraints.NotBlank;

public record BankTransfer(@NotBlank String iban, boolean instantSettlement) implements PaymentMethod {
}

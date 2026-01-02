package com.example.ordertracker.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank(message = "Description is required")
    private String description;
}

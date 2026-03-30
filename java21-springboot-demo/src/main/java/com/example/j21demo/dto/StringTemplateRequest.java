package com.example.j21demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StringTemplateRequest(@NotBlank String teamName, @NotBlank String owner, @Min(0) int completedExamples) {
}
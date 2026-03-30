package com.example.j21demo.dto;

public record PaymentDecision(String paymentType, String routing, boolean fraudReviewRequired, String takeaway) {
}
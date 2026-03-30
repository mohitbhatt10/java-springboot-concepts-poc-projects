package com.example.j21demo.dto;

import java.util.List;

public record StructuredConcurrencyResponse(String inventoryStatus, String pricingDecision,
        List<String> workerThreads, long elapsedMillis, String takeaway) {
}
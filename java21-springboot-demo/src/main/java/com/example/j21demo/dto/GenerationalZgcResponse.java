package com.example.j21demo.dto;

import java.util.List;

public record GenerationalZgcResponse(String recommendedJvmArgs, String intent, List<String> monitoringSignals,
        String takeaway) {
}
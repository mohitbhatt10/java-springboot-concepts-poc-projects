package com.example.j21demo.dto;

import java.util.List;

public record VirtualThreadsResponse(String mode, List<String> threadNames, long elapsedMillis, String takeaway) {
}
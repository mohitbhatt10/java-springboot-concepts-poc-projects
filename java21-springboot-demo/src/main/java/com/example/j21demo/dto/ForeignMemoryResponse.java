package com.example.j21demo.dto;

import java.util.List;

public record ForeignMemoryResponse(List<Integer> storedValues, int sum, long bytesAllocated, String takeaway) {
}
package com.example.j21demo.dto;

public record ScopedValueResponse(String requestId, String parentThread, String childThread,
                                  String childObservedRequestId, String takeaway) {
}
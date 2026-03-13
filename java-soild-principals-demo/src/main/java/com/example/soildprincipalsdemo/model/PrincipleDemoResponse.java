package com.example.soildprincipalsdemo.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Standard API response model used by all SOLID demo endpoints.
 *
 * We keep a single response envelope so the API stays predictable and easy to
 * read.
 */
public class PrincipleDemoResponse {

    /** Name of the SOLID principle that this response corresponds to. */
    private String principle;

    /** High-level status (for demo readability), e.g., PASS. */
    private String status;

    /** Human-readable summary message. */
    private String message;

    /** Flexible key-value details specific to each principle demo. */
    private Map<String, Object> details;

    /**
     * Default constructor required by Jackson for JSON
     * serialization/deserialization.
     */
    public PrincipleDemoResponse() {
        this.details = new LinkedHashMap<>();
    }

    /**
     * Convenience constructor to initialize primary response fields.
     */
    public PrincipleDemoResponse(String principle, String status, String message) {
        this.principle = principle;
        this.status = status;
        this.message = message;
        this.details = new LinkedHashMap<>();
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    /**
     * Builder-like helper method to add details in a fluent way.
     */
    public PrincipleDemoResponse addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }
}

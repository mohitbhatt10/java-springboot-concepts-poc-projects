package com.example.j21demo.dto;

import java.util.List;

public record SequencedCollectionsResponse(List<String> workflow, List<String> reversedWorkflow,
        String firstMilestone, String lastMilestone,
        List<String> regionOrder, String takeaway) {
}
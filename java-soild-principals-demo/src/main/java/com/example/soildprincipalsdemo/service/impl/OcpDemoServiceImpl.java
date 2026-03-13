package com.example.soildprincipalsdemo.service.impl;

import com.example.soildprincipalsdemo.model.OcpRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;
import com.example.soildprincipalsdemo.service.OcpDemoService;
import com.example.soildprincipalsdemo.solid.ocp.DiscountStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * OCP demonstration service implementation.
 *
 * The service depends on DiscountStrategy abstraction and remains closed for
 * modification when new strategies are introduced.
 */
@Service
public class OcpDemoServiceImpl implements OcpDemoService {

    private final Map<String, DiscountStrategy> strategyMap;

    public OcpDemoServiceImpl(List<DiscountStrategy> strategies) {
        // Step 1: Convert list of strategy beans into a lookup map keyed by type.
        // This avoids if-else chains and keeps the service extension-friendly.
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(s -> s.getType().toUpperCase(), Function.identity()));
    }

    @Override
    public PrincipleDemoResponse demonstrate(OcpRequest request) {
        // Step 2: Normalize incoming key.
        String customerType = request.getCustomerType().toUpperCase();

        // Step 3: Resolve strategy based on key. Throw if unknown for explicit
        // feedback.
        DiscountStrategy strategy = strategyMap.get(customerType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported customerType: " + request.getCustomerType());
        }

        // Step 4: Execute strategy behavior through abstraction.
        double finalAmount = strategy.apply(request.getAmount());

        // Step 5: Return explainable demo response.
        return new PrincipleDemoResponse("OCP", "PASS",
                "Extended behavior via strategy without modifying service logic.")
                .addDetail("customerType", customerType)
                .addDetail("originalAmount", request.getAmount())
                .addDetail("finalAmount", finalAmount)
                .addDetail("strategyUsed", strategy.getClass().getSimpleName());
    }
}

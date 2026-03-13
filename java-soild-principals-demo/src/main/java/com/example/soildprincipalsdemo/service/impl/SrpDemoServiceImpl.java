package com.example.soildprincipalsdemo.service.impl;

import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;
import com.example.soildprincipalsdemo.service.SrpDemoService;
import com.example.soildprincipalsdemo.solid.srp.OrderData;
import com.example.soildprincipalsdemo.solid.srp.OrderNotifier;
import com.example.soildprincipalsdemo.solid.srp.OrderRepositorySimulator;
import com.example.soildprincipalsdemo.solid.srp.OrderValidator;
import org.springframework.stereotype.Service;

/**
 * SRP demonstration service implementation.
 *
 * This orchestrator coordinates multiple classes where each class has one
 * reason
 * to change (validation, persistence simulation, notification).
 */
@Service
public class SrpDemoServiceImpl implements SrpDemoService {

    private final OrderValidator orderValidator;
    private final OrderRepositorySimulator orderRepositorySimulator;
    private final OrderNotifier orderNotifier;

    public SrpDemoServiceImpl(OrderValidator orderValidator,
            OrderRepositorySimulator orderRepositorySimulator,
            OrderNotifier orderNotifier) {
        this.orderValidator = orderValidator;
        this.orderRepositorySimulator = orderRepositorySimulator;
        this.orderNotifier = orderNotifier;
    }

    @Override
    public PrincipleDemoResponse demonstrate() {
        // Step 1: Create sample order data for demonstration.
        OrderData orderData = new OrderData("ORD-1001", "demo.user@example.com", 1200.0);

        // Step 2: Delegate validation to validator class (single responsibility:
        // validation).
        orderValidator.validate(orderData);

        // Step 3: Delegate persistence action to repository simulator class.
        String persistenceTrace = orderRepositorySimulator.save(orderData);

        // Step 4: Delegate notification action to notifier class.
        String notificationTrace = orderNotifier.notifyCustomer(orderData);

        // Step 5: Build a rich response to explain what happened.
        return new PrincipleDemoResponse("SRP", "PASS", "Each class has one responsibility in this flow.")
                .addDetail("orderId", orderData.getOrderId())
                .addDetail("validation", "Order data validated successfully")
                .addDetail("persistence", persistenceTrace)
                .addDetail("notification", notificationTrace);
    }
}

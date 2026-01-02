package com.example.ordertracker.service;

import com.example.ordertracker.model.Order;
import com.example.ordertracker.model.OrderEvent;
import com.example.ordertracker.model.OrderRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<Order> createOrder(OrderRequest request);

    Mono<Order> getOrder(String id);

    Flux<OrderEvent> trackOrder(String id);
}

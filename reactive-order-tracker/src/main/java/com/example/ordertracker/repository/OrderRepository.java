package com.example.ordertracker.repository;

import com.example.ordertracker.model.Order;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class OrderRepository {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    public Mono<Order> save(Order order) {
        store.put(order.getId(), order);
        return Mono.just(order);
    }

    public Mono<Order> findById(String id) {
        return Mono.justOrEmpty(store.get(id));
    }

    public Flux<Order> findAll() {
        return Flux.fromIterable(store.values());
    }
}

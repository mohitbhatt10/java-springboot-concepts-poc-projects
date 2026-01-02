package com.example.ordertracker.service.impl;

import com.example.ordertracker.exception.OrderNotFoundException;
import com.example.ordertracker.model.Order;
import com.example.ordertracker.model.OrderEvent;
import com.example.ordertracker.model.OrderRequest;
import com.example.ordertracker.model.OrderStatus;
import com.example.ordertracker.publisher.OrderStatusPublisher;
import com.example.ordertracker.repository.OrderRepository;
import com.example.ordertracker.service.OrderService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusPublisher statusPublisher;

    @Override
    public Mono<Order> createOrder(OrderRequest request) {
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .description(request.getDescription())
                .createdAt(Instant.now())
                .status(OrderStatus.CREATED)
                .build();

        return orderRepository.save(order)
                .doOnSuccess(o -> log.info("[service] created order {} desc='{}'", o.getId(), o.getDescription()));
    }

    @Override
    public Mono<Order> getOrder(String id) {
        return orderRepository.findById(id)
                .doOnSubscribe(sub -> log.debug("[service] fetching order {}", id))
                .switchIfEmpty(Mono.error(new OrderNotFoundException(id)));
    }

    @Override
    public Flux<OrderEvent> trackOrder(String id) {
        return getOrder(id)
                .doOnSuccess(o -> log.info("[service] start tracking order {} currentStatus={} createdAt={} desc='{}'",
                        o.getId(), o.getStatus(), o.getCreatedAt(), o.getDescription()))
                .flatMapMany(order -> statusPublisher.streamStatuses(order)
                        .flatMap(event -> orderRepository.save(order.toBuilder()
                                .status(event.getStatus())
                                .build())
                                .doOnSuccess(saved -> log.debug("[service] persisted status {} for order {}",
                                        saved.getStatus(), saved.getId()))
                                .thenReturn(event))
                        .onErrorResume(throwable -> {
                            log.warn("[service] stream error for order {}: {}", order.getId(), throwable.toString());
                            return Flux.just(OrderEvent.builder()
                                    .orderId(order.getId())
                                    .status(order.getStatus())
                                    .timestamp(Instant.now())
                                    .message("Stream error: " + throwable.getMessage())
                                    .error(true)
                                    .build());
                        })
                        .doOnComplete(() -> log.info("[service] tracking completed for order {}", order.getId()))
                        .doOnCancel(() -> log.warn("[service] tracking cancelled for order {}", order.getId())));
    }
}

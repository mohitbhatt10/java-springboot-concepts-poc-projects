package com.example.ordertracker.publisher;

import com.example.ordertracker.model.Order;
import com.example.ordertracker.model.OrderEvent;
import com.example.ordertracker.model.OrderStatus;
import com.example.ordertracker.util.OrderStatusGenerator;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusPublisher {

    private static final Duration BASE_DELAY = Duration.ofSeconds(3);
    private static final Random RANDOM = new Random();

    public Flux<OrderEvent> streamStatuses(Order order) {
        if (order.getStatus() == OrderStatus.DELIVERED) {
            log.info("[publisher] order {} already DELIVERED; emitting terminal event only", order.getId());
            return Flux.just(OrderEvent.builder()
                    .orderId(order.getId())
                    .status(OrderStatus.DELIVERED)
                    .timestamp(Instant.now())
                    .message("Order already delivered")
                    .error(false)
                    .build());
        }

        return Flux.fromIterable(OrderStatusGenerator.progressionFrom(order.getStatus()))
                .concatMap(status -> Mono.delay(jitteredDelay())
                        .doOnSubscribe(sub -> log.debug("[publisher] scheduling status {} for order {}", status,
                                order.getId()))
                        .flatMapMany(ignore -> maybeError(status)))
                .map(status -> OrderEvent.builder()
                        .orderId(order.getId())
                        .status(status)
                        .timestamp(Instant.now())
                        .message("Order status updated to " + status)
                        .error(false)
                        .build())
                .doOnSubscribe(sub -> log.info("[publisher] start streaming order {} from status {}", order.getId(),
                        order.getStatus()))
                .doOnNext(evt -> log.debug("[publisher] emit {} for order {} at {}", evt.getStatus(), evt.getOrderId(),
                    evt.getTimestamp()))
                .doOnError(err -> log.warn("[publisher] error for order {}: {}", order.getId(), err.toString()))
                .doOnComplete(() -> log.info("[publisher] completed stream for order {}", order.getId()))
                .takeUntil(evt -> evt.getStatus() == OrderStatus.DELIVERED)
                .onBackpressureLatest();
    }

    private Duration jitteredDelay() {
        long jitterMillis = RANDOM.nextInt(1500);
        Duration d = BASE_DELAY.plusMillis(jitterMillis);
        log.trace("[publisher] jittered delay {} ms", d.toMillis());
        return d;
    }

    private Flux<OrderStatus> maybeError(OrderStatus status) {
        if (shouldFail(status)) {
            return Flux.error(new IllegalStateException("Simulated transient issue at " + status));
        }
        return Flux.just(status);
    }

    private boolean shouldFail(OrderStatus status) {
        if (status == OrderStatus.CREATED) {
            return false;
        }
        return RANDOM.nextDouble() < 0.08;
    }
}

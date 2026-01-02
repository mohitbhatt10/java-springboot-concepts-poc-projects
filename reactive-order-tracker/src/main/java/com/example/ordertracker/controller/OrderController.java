package com.example.ordertracker.controller;

import com.example.ordertracker.model.Order;
import com.example.ordertracker.model.OrderEvent;
import com.example.ordertracker.model.OrderRequest;
import com.example.ordertracker.service.OrderService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@Validated @RequestBody OrderRequest request) {
        return orderService.createOrder(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable("id") String id) {
        return orderService.getOrder(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "/{id}/track", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<OrderEvent>> track(@PathVariable("id") String id) {
        log.info("[controller] SSE subscribe for order {}", id);
        Flux<ServerSentEvent<OrderEvent>> statusFlux = orderService.trackOrder(id)
                .doOnSubscribe(sub -> log.debug("[controller] statusFlux subscribed for order {}", id))
                .map(event -> ServerSentEvent.<OrderEvent>builder()
                        .event(event.isError() ? "stream-error" : "status")
                        .id(event.getTimestamp() != null ? String.valueOf(event.getTimestamp().toEpochMilli()) : null)
                        .data(event)
                        .build())
                .onErrorResume(throwable -> Flux.just(ServerSentEvent.<OrderEvent>builder()
                        .event("stream-error")
                        .data(OrderEvent.builder()
                                .orderId(id)
                                .message("Stream failed: " + throwable.getMessage())
                                .error(true)
                                .build())
                        .build()))
                .concatWith(Mono.delay(Duration.ofSeconds(15))
                        .then(Mono.just(ServerSentEvent.<OrderEvent>builder()
                                .event("complete")
                                .build())))
                .doOnNext(evt -> log.debug("[controller] sending SSE event {} for order {}", evt.event(), id))
                .doOnComplete(() -> log.info("[controller] statusFlux complete for order {}", id))
                .doOnCancel(() -> log.warn("[controller] statusFlux cancelled for order {}", id))
                .share();

        Flux<ServerSentEvent<OrderEvent>> heartbeat = Flux.interval(Duration.ofSeconds(2))
                .map(tick -> ServerSentEvent.<OrderEvent>builder()
                        .event("ping")
                        .data(OrderEvent.builder()
                                .message("keep-alive")
                                .build())
                        .build())
                .takeUntilOther(statusFlux
                        .filter(sse -> "complete".equals(sse.event()))
                        .next());

        return Flux.merge(statusFlux, heartbeat)
                .doOnCancel(() -> log.warn("[controller] SSE cancelled for order {}", id))
                .doOnComplete(() -> log.info("[controller] SSE complete for order {}", id));
    }
}

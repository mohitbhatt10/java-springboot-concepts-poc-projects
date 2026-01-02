# Reactive Order Tracker

A Spring Boot 3.x (WebFlux) demo that showcases reactive programming, Server-Sent Events (SSE), and a minimal Thymeleaf UI styled with Tailwind. Users can create an order and watch it progress through statuses in real time without polling.

## Why Reactive & WebFlux?

- **Reactive programming** models streams of data and signals (values, completion, errors) over time, letting us compose asynchronous, non-blocking pipelines.
- **Backpressure** ensures slow consumers do not get overwhelmed; publishers respect demand and can drop/buffer as configured.
- **Spring WebFlux** is built on Reactor and Netty, delivering fully non-blocking I/O and reactive types end-to-end.
- **SSE** keeps a single HTTP connection open for one-way event streaming from server to browser with minimal overhead.

### Mono vs Flux (Reactor)

- **Mono**: 0..1 element, completes or errors. Ideal for single resources (e.g., create/find an order).
- **Flux**: 0..N elements, completes or errors. Ideal for streams (e.g., status updates).

## Architecture

- **Order lifecycle**: `CREATED → CONFIRMED → PACKED → OUT_FOR_DELIVERY → DELIVERED`
- **OrderStatusPublisher** streams statuses every ~2 seconds with jitter, simulates occasional transient errors, and demonstrates backpressure handling (`onBackpressureLatest`).
- **Repository**: in-memory `ConcurrentHashMap`, exposed with reactive types.
- **Transport**: SSE (`text/event-stream`) to the browser; errors propagate as SSE `error` events; completion is signaled explicitly.
- **UI**: Thymeleaf pages (`index` to create, `order` to track) + Tailwind + theme toggle (dark/light stored in `localStorage`).

## Project layout

```
reactive-order-tracker
├── pom.xml
├── README.md
└── src/main
    ├── java/com/example/ordertracker
    │   ├── ReactiveOrderTrackerApplication.java
    │   ├── controller
    │   │   ├── OrderController.java
    │   │   └── ViewController.java
    │   ├── service
    │   │   ├── OrderService.java
    │   │   └── impl/OrderServiceImpl.java
    │   ├── publisher/OrderStatusPublisher.java
    │   ├── repository/OrderRepository.java
    │   ├── model
    │   │   ├── Order.java
    │   │   ├── OrderRequest.java
    │   │   ├── OrderEvent.java
    │   │   └── OrderStatus.java
    │   ├── exception
    │   │   ├── OrderNotFoundException.java
    │   │   └── GlobalExceptionHandler.java
    │   └── util/OrderStatusGenerator.java
    └── resources
        ├── application.yml
        ├── templates
        │   ├── index.html
        │   └── order.html
        └── static
            ├── css/tailwind.css
            └── js/theme-toggle.js
```

## Running the app

```bash
mvn spring-boot:run
```

Then open http://localhost:8080.

## Using the demo

1. On **Home**, enter a description and submit.
2. Follow the generated tracking link.
3. The tracking page opens an `EventSource` to `/orders/{id}/track` and renders updates live.
4. Toggle dark/light theme via the button (persisted in `localStorage`).

## SSE testing tips

- In the browser devtools console:
  ```js
  const s = new EventSource("/orders/{id}/track");
  s.onmessage = console.log;
  s.onerror = console.error;
  ```
- Curl stream:
  ```bash
  curl -N http://localhost:8080/orders/{id}/track
  ```

## Logging & debugging

- `application.yml` enables DEBUG for WebFlux/Netty to visualize request/response flow and backpressure.
- `OrderStatusPublisher` injects jitter and occasional errors; `Retry.max(1)` shows recovery once, then surfaces the error.

## Notes

- Tailwind is loaded via CDN for simplicity; a custom `tailwind.css` adds small tweaks and focus styles.
- Repository is in-memory for demo purposes; swap with a reactive datastore (e.g., R2DBC, MongoDB) for persistence.

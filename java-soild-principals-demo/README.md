# java-soild-principals-demo

A Spring Boot 3.4.2 + Java 17 project that demonstrates **SOLID** principles using controller-driven HTTP endpoints.

> Naming note: This project intentionally keeps the requested folder/artifact naming (`soild` and `principals`) as provided.

## Tech Stack

- Java 17
- Spring Boot 3.4.2
- Spring Web
- Spring Validation
- Maven

## Project Goal

This project explains each SOLID principle through small, focused examples:

- **S** - Single Responsibility Principle
- **O** - Open/Closed Principle
- **L** - Liskov Substitution Principle
- **I** - Interface Segregation Principle
- **D** - Dependency Inversion Principle

Each class contains verbose inline comments describing what it does step by step.

## API Base Path

- `/api/solid`

## Endpoints

### 1) SRP Demo

- **GET** `/api/solid/srp`
- Demonstrates splitting validation, persistence simulation, and notification into separate classes.

### 2) OCP Demo

- **POST** `/api/solid/ocp`
- Request:

```json
{
  "customerType": "PREMIUM",
  "amount": 1000
}
```

- Demonstrates strategy-based extension without changing service logic.

### 3) LSP Demo

- **POST** `/api/solid/lsp`
- Request:

```json
{
  "shape": "RECTANGLE",
  "width": 10,
  "height": 4
}
```

- Demonstrates substituting `Rectangle`/`Square` using the same `Shape` abstraction.

### 4) ISP Demo

- **POST** `/api/solid/isp`
- Request:

```json
{
  "workerType": "HUMAN",
  "task": "pack-orders"
}
```

- Demonstrates splitting capabilities into small interfaces (`Workable`, `Eatable`).

### 5) DIP Demo

- **POST** `/api/solid/dip`
- Request:

```json
{
  "channel": "EMAIL",
  "recipient": "demo.user@example.com",
  "message": "Order shipped"
}
```

- Demonstrates depending on the `MessageSender` abstraction, not concrete sender classes.

## Run Locally

From this project folder:

1. Build:
   - `mvn clean install`
2. Run:
   - `mvn spring-boot:run`
3. App URL:
   - `http://localhost:8082`

## Validation and Error Handling

- Request payloads are validated via Bean Validation annotations.
- Global error payloads are returned from `GlobalExceptionHandler`.

## Suggested Learning Flow

1. Start with SRP endpoint to understand responsibility separation.
2. Move to OCP and add a new strategy class to see extension in action.
3. Use LSP endpoint with different shape types and observe consistent contract usage.
4. Use ISP endpoint with both worker types and inspect behavior differences.
5. Use DIP endpoint and then add a new sender implementation to extend behavior cleanly.

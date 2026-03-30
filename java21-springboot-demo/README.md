# Java 21 Features Demo on Spring Boot

This project has been refactored into a feature-oriented demo API that shows the requested Java 21 topics with concrete Spring Boot examples. The code is compiled and run in this workspace with JDK 22 because several requested features are still preview features and must be compiled with the active preview-enabled JDK.

## Covered topics

- Virtual Threads
- Structured Concurrency
- Pattern Matching Switch
- Record Patterns
- Sequenced Collections
- String Templates
- Scoped Values
- Foreign Memory API
- Generational ZGC guidance

## Environment

Use this JDK for build and runtime:

```powershell
$env:JAVA_HOME='C:\Program Files\OpenLogic\jdk-22.0.2.9-hotspot'
$env:Path="$env:JAVA_HOME\bin;" + $env:Path
```

Compile and test:

```powershell
mvn clean test
```

Run the application:

```powershell
mvn spring-boot:run
```

Run with Generational ZGC enabled:

```powershell
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--enable-preview -XX:+UseZGC -XX:+ZGenerational"
```

The Maven build already enables preview features, so no extra compiler flags are needed on the command line.

## API endpoints

- `GET /api/java21`: feature catalog with endpoint descriptions and maturity levels.
- `GET /api/java21/threads/platform`: blocking workflow on platform threads.
- `GET /api/java21/threads/virtual`: same workflow on virtual threads.
- `GET /api/java21/structured-concurrency`: concurrent subtasks with `StructuredTaskScope`.
- `POST /api/java21/pattern-matching-switch`: sealed type routing for payments.
- `POST /api/java21/record-patterns`: nested record destructuring for order fulfillment.
- `GET /api/java21/sequenced-collections`: first, last, and reversed ordered collection operations.
- `POST /api/java21/string-templates`: preview string interpolation example.
- `GET /api/java21/scoped-values?requestId=req-1001`: scoped request context propagation.
- `GET /api/java21/foreign-memory`: off-heap memory segment allocation and reading.
- `GET /api/java21/generational-zgc`: runtime configuration guidance for low-pause workloads.
- `GET /api/java21/sse`: lightweight server-sent events stream.

## Sample payloads

Pattern matching switch:

```json
{
  "type": "credit-card",
  "number": "9123456789012345",
  "corporateCard": false
}
```

Record patterns:

```json
{
  "type": "physical",
  "customer": {
    "customerId": "cust-101",
    "tier": "gold"
  },
  "shippingAddress": {
    "city": "Pune",
    "country": "India"
  },
  "items": ["Java 21 Book", "Spring Boot Guide"]
}
```

String templates:

```json
{
  "teamName": "Platform Enablement",
  "owner": "Mohit",
  "completedExamples": 9
}
```

## Notes

- `StringTemplate`, `StructuredTaskScope`, `ScopedValue`, and the Foreign Memory API are preview APIs in the JDK 22 toolchain used here.
- Generational ZGC is a JVM capability, so the demo exposes operational guidance rather than a fake business endpoint.
- The project remains a Spring Boot learning/demo app, but the structure is now organized around feature services and typed DTOs instead of a single controller full of ad hoc logic.

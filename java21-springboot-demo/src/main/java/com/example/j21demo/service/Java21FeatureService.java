package com.example.j21demo.service;

import com.example.j21demo.dto.FeatureCatalogItem;
import com.example.j21demo.dto.FeatureCatalogResponse;
import com.example.j21demo.dto.ForeignMemoryResponse;
import com.example.j21demo.dto.GenerationalZgcResponse;
import com.example.j21demo.dto.OrderAnalysis;
import com.example.j21demo.dto.PaymentDecision;
import com.example.j21demo.dto.ScopedValueResponse;
import com.example.j21demo.dto.SequencedCollectionsResponse;
import com.example.j21demo.dto.StringTemplateRequest;
import com.example.j21demo.dto.StringTemplateResponse;
import com.example.j21demo.dto.StructuredConcurrencyResponse;
import com.example.j21demo.dto.VirtualThreadsResponse;
import com.example.j21demo.model.BankTransfer;
import com.example.j21demo.model.CreditCard;
import com.example.j21demo.model.DemoOrder;
import com.example.j21demo.model.DigitalOrder;
import com.example.j21demo.model.PaymentMethod;
import com.example.j21demo.model.PhysicalOrder;
import org.springframework.stereotype.Service;

import java.lang.ScopedValue;
import java.lang.StringTemplate;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class Java21FeatureService {

    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    public FeatureCatalogResponse catalog() {
        return new FeatureCatalogResponse(List.of(
                new FeatureCatalogItem("Virtual Threads", "/api/java21/threads/virtual", "Standard",
                        "Compares platform and virtual thread execution for blocking work."),
                new FeatureCatalogItem("Structured Concurrency", "/api/java21/structured-concurrency", "Preview",
                        "Runs related tasks as one unit with failure propagation."),
                new FeatureCatalogItem("Pattern Matching Switch", "/api/java21/pattern-matching-switch", "Standard",
                        "Routes a sealed payment hierarchy using typed switch cases."),
                new FeatureCatalogItem("Record Patterns", "/api/java21/record-patterns", "Standard",
                        "Destructures nested records in a switch for fulfillment decisions."),
                new FeatureCatalogItem("Sequenced Collections", "/api/java21/sequenced-collections", "Standard",
                        "Shows first/last/reversed operations on ordered collections."),
                new FeatureCatalogItem("String Templates", "/api/java21/string-templates", "Preview",
                        "Builds structured messages with embedded expressions."),
                new FeatureCatalogItem("Scoped Values", "/api/java21/scoped-values", "Preview",
                        "Propagates request context safely across structured tasks."),
                new FeatureCatalogItem("Foreign Memory API", "/api/java21/foreign-memory", "Preview",
                        "Allocates and reads native memory with memory segments."),
                new FeatureCatalogItem("Generational ZGC", "/api/java21/generational-zgc", "Runtime",
                        "Documents the JVM flags and signals used to run low-latency workloads.")));
    }

    public VirtualThreadsResponse threadComparison(String mode) throws Exception {
        return switch (mode.toLowerCase()) {
            case "platform" -> executeThreadTasks("platform", Executors.newFixedThreadPool(3));
            case "virtual" -> executeThreadTasks("virtual", Executors.newVirtualThreadPerTaskExecutor());
            default -> throw new IllegalArgumentException("Unsupported mode: " + mode + ". Use platform or virtual.");
        };
    }

    public StructuredConcurrencyResponse structuredConcurrency() throws Exception {
        Instant started = Instant.now();
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var inventoryTask = scope.fork(simulatedThreadTask("inventory-ready", 90));
            var pricingTask = scope.fork(simulatedThreadTask("dynamic-discount-eligible", 120));

            scope.joinUntil(Instant.now().plusSeconds(5));
            scope.throwIfFailed();

            return new StructuredConcurrencyResponse(
                    inventoryTask.get().result(),
                    pricingTask.get().result(),
                    List.of(inventoryTask.get().threadName(), pricingTask.get().threadName()),
                    Duration.between(started, Instant.now()).toMillis(),
                    "StructuredTaskScope keeps sibling tasks bounded to the same request and failure policy.");
        } catch (TimeoutException ex) {
            throw new IllegalStateException("Structured concurrency example timed out", ex);
        }
    }

    public PaymentDecision patternMatchingSwitch(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case CreditCard(String number, boolean corporateCard) -> new PaymentDecision(
                    "credit-card",
                    corporateCard ? "corporate-ledger" : "card-gateway",
                    !corporateCard && number.startsWith("9"),
                    "Pattern matching switch removes casts and keeps sealed hierarchy handling exhaustive.");
            case BankTransfer(String iban, boolean instantSettlement) -> new PaymentDecision(
                    "bank-transfer",
                    instantSettlement ? "instant-clearing" : "standard-clearing",
                    iban.startsWith("DE89"),
                    "The compiler enforces that every permitted payment type is handled.");
        };
    }

    public OrderAnalysis recordPatterns(DemoOrder order) {
        return switch (order) {
            case PhysicalOrder(var customer, var address, var items) when "gold".equalsIgnoreCase(customer.tier()) ->
                new OrderAnalysis(
                        "priority-physical",
                        items.size() + " items shipping to " + address.city() + ", " + address.country(),
                        "Record patterns destructure nested records directly inside the switch case.");
            case PhysicalOrder(var customer, var address, var items) -> new OrderAnalysis(
                    "standard-physical",
                    items.size() + " items queued for regional warehouse near " + address.city(),
                    "The same record shape can be matched with different guards for business rules.");
            case DigitalOrder(var customer, String downloadEmail, var entitlements) -> new OrderAnalysis(
                    "digital-fulfillment",
                    entitlements.size() + " licenses will be issued to " + downloadEmail,
                    "Nested record destructuring makes DTO-heavy workflows easier to read.");
        };
    }

    public SequencedCollectionsResponse sequencedCollections() {
        var workflow = new ArrayList<>(List.of("catalog", "checkout", "payment", "fulfillment"));
        workflow.addFirst("landing-page");
        workflow.addLast("follow-up-email");

        var regionCapacity = new LinkedHashMap<String, Integer>();
        regionCapacity.put("apac", 4);
        regionCapacity.put("emea", 7);
        regionCapacity.put("amer", 5);

        return new SequencedCollectionsResponse(
                List.copyOf(workflow),
                List.copyOf(workflow.reversed()),
                workflow.getFirst(),
                workflow.getLast(),
                List.copyOf(regionCapacity.sequencedKeySet()),
                "Sequenced collections standardize first/last/reversed access across ordered collection types.");
    }

    public StringTemplateResponse stringTemplates(StringTemplateRequest request) {
        // String templates remain a preview feature under JDK 22.
        String renderedTemplate = StringTemplate.STR."Team \{request.teamName()} is owned by \{request.owner()} and has \{request.completedExamples()} Java 21 examples wired.";
        return new StringTemplateResponse(
                renderedTemplate,
                "String templates keep interpolation readable while preserving typed embedded expressions.");
    }

    public ScopedValueResponse scopedValues(String requestId) throws Exception {
        return ScopedValue.where(REQUEST_ID, requestId).call(() -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                var childTask = scope.fork(() -> new ScopedValueWorker(
                        Thread.currentThread().getName(),
                        REQUEST_ID.isBound() ? REQUEST_ID.get() : "unbound"));

                scope.joinUntil(Instant.now().plusSeconds(5));
                scope.throwIfFailed();

                var worker = childTask.get();
                return new ScopedValueResponse(
                        requestId,
                        Thread.currentThread().getName(),
                        worker.threadName(),
                        worker.requestId(),
                        "Scoped values provide explicit request context propagation without mutable ThreadLocal state.");
            } catch (TimeoutException ex) {
                throw new IllegalStateException("Scoped value example timed out", ex);
            }
        });
    }

    public ForeignMemoryResponse foreignMemory() {
        try (var arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(ValueLayout.JAVA_INT, 3);
            segment.setAtIndex(ValueLayout.JAVA_INT, 0, 21);
            segment.setAtIndex(ValueLayout.JAVA_INT, 1, 44);
            segment.setAtIndex(ValueLayout.JAVA_INT, 2, 21);
            var values = new ArrayList<Integer>();
            for (long index = 0; index < 3; index++) {
                values.add(segment.getAtIndex(ValueLayout.JAVA_INT, index));
            }
            int sum = values.stream().mapToInt(Integer::intValue).sum();
            return new ForeignMemoryResponse(
                    List.copyOf(values),
                    sum,
                    segment.byteSize(),
                    "The foreign memory API replaces error-prone off-heap access patterns with bounded memory segments.");
        }
    }

    public GenerationalZgcResponse generationalZgc() {
        return new GenerationalZgcResponse(
                "--enable-preview -XX:+UseZGC -XX:+ZGenerational",
                "Use it for low-pause services that allocate many short-lived objects.",
                List.of("gc+heap logs", "allocation rate", "pause time percentile", "live-set growth"),
                "Generational ZGC is a JVM runtime choice, so the right example is startup guidance and observability rather than application code.");
    }

    public String sseMessage(long sequence) {
        return "java21-feature-tick-" + sequence;
    }

    private VirtualThreadsResponse executeThreadTasks(String mode, java.util.concurrent.ExecutorService executorService)
            throws Exception {
        Instant started = Instant.now();
        try (executorService) {
            List<Future<String>> futures = List.of(
                    executorService.submit(threadTask("catalog-load")),
                    executorService.submit(threadTask("pricing-check")),
                    executorService.submit(threadTask("inventory-sync")));

            List<String> threadNames = new ArrayList<>();
            for (Future<String> future : futures) {
                threadNames.add(future.get(5, TimeUnit.SECONDS));
            }

            String takeaway = "virtual".equals(mode)
                    ? "Virtual threads are cheap enough to model blocking work as one task per request concern."
                    : "Platform threads stay useful as a comparison baseline for the same blocking workflow.";

            return new VirtualThreadsResponse(mode, threadNames,
                    Duration.between(started, Instant.now()).toMillis(), takeaway);
        } catch (TimeoutException ex) {
            throw new IllegalStateException("Thread comparison example timed out", ex);
        }
    }

    private Callable<String> threadTask(String label) {
        return () -> {
            Thread.sleep(75);
            return label + "@" + Thread.currentThread().getName();
        };
    }

    private Callable<StructuredTaskResult> simulatedThreadTask(String result, long delayMillis) {
        return () -> {
            Thread.sleep(delayMillis);
            return new StructuredTaskResult(result, Thread.currentThread().getName());
        };
    }

    private record ScopedValueWorker(String threadName, String requestId) {
    }

    private record StructuredTaskResult(String result, String threadName) {
    }
}
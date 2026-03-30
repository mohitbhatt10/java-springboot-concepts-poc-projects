package com.example.j21demo.controller;

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
import com.example.j21demo.model.DemoOrder;
import com.example.j21demo.model.PaymentMethod;
import com.example.j21demo.service.Java21FeatureService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/api/java21")
public class DemoController {

    private final Java21FeatureService featureService;

    public DemoController(Java21FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping
    public FeatureCatalogResponse catalog() {
        return featureService.catalog();
    }

    @GetMapping("/threads/{mode}")
    public VirtualThreadsResponse threadComparison(@PathVariable String mode) throws Exception {
        return featureService.threadComparison(mode);
    }

    @GetMapping("/structured-concurrency")
    public StructuredConcurrencyResponse structuredConcurrency() throws Exception {
        return featureService.structuredConcurrency();
    }

    @PostMapping("/pattern-matching-switch")
    public PaymentDecision patternMatchingSwitch(@Valid @RequestBody PaymentMethod paymentMethod) {
        return featureService.patternMatchingSwitch(paymentMethod);
    }

    @PostMapping("/record-patterns")
    public OrderAnalysis recordPatterns(@Valid @RequestBody DemoOrder order) {
        return featureService.recordPatterns(order);
    }

    @GetMapping("/sequenced-collections")
    public SequencedCollectionsResponse sequencedCollections() {
        return featureService.sequencedCollections();
    }

    @PostMapping("/string-templates")
    public StringTemplateResponse stringTemplates(@Valid @RequestBody StringTemplateRequest request) {
        return featureService.stringTemplates(request);
    }

    @GetMapping("/scoped-values")
    public ScopedValueResponse scopedValues(@RequestParam(defaultValue = "request-java21-001") String requestId)
            throws Exception {
        return featureService.scopedValues(requestId);
    }

    @GetMapping("/foreign-memory")
    public ForeignMemoryResponse foreignMemory() {
        return featureService.foreignMemory();
    }

    @GetMapping("/generational-zgc")
    public GenerationalZgcResponse generationalZgc() {
        return featureService.generationalZgc();
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sse() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> featureService.sseMessage(sequence))
                .onErrorReturn("java21-feature-tick-error")
                .delayElements(Duration.ofMillis(150));
    }
}

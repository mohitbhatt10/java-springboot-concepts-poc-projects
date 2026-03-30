package com.example.j21demo.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PhysicalOrder.class, name = "physical"),
        @JsonSubTypes.Type(value = DigitalOrder.class, name = "digital")
})
public sealed interface DemoOrder permits PhysicalOrder, DigitalOrder {
}
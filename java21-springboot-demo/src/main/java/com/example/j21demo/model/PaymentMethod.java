package com.example.j21demo.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreditCard.class, name = "credit-card"),
        @JsonSubTypes.Type(value = BankTransfer.class, name = "bank-transfer")
})
public sealed interface PaymentMethod permits CreditCard, BankTransfer {
}

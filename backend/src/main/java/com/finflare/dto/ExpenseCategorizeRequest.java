package com.finflare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ExpenseCategorizeRequest {
    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double amount;

    // Constructors
    public ExpenseCategorizeRequest() {}

    public ExpenseCategorizeRequest(String description, Double amount) {
        this.description = description;
        this.amount = amount;
    }

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
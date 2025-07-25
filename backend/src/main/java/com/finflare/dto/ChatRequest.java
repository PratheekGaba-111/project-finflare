package com.finflare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChatRequest {
    @NotBlank
    @Size(max = 1000)
    private String message;

    private String context;

    // Constructors
    public ChatRequest() {}

    public ChatRequest(String message) {
        this.message = message;
    }

    public ChatRequest(String message, String context) {
        this.message = message;
        this.context = context;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
}
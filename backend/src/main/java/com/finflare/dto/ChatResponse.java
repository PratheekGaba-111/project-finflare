package com.finflare.dto;

import java.time.LocalDateTime;

public class ChatResponse {
    private String message;
    private LocalDateTime timestamp;
    private String type = "ai";

    // Constructors
    public ChatResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ChatResponse(String message, String type) {
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
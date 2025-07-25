package com.finflare.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class AIService {

    private final WebClient webClient;

    @Value("${app.openai.api.key:}")
    private String openaiApiKey;

    @Value("${app.openai.api.url:https://api.openai.com/v1}")
    private String openaiApiUrl;

    public AIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(openaiApiUrl)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<String> categorizeExpense(String description, double amount) {
        String prompt = String.format(
            "Categorize this expense into one of these categories: " +
            "FOOD_DINING, TRANSPORTATION, SHOPPING, ENTERTAINMENT, BILLS_UTILITIES, " +
            "HEALTHCARE, EDUCATION, TRAVEL, GROCERIES, INSURANCE, INVESTMENTS, " +
            "GIFTS_DONATIONS, PERSONAL_CARE, HOME_GARDEN, BUSINESS, OTHER. " +
            "Expense: %s (Amount: $%.2f). " +
            "Return only the category name, nothing else.",
            description, amount
        );

        Map<String, Object> requestBody = createChatCompletionRequest(prompt, 50);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::extractMessageContent)
                .onErrorReturn("OTHER");
    }

    public Mono<String> getChatbotResponse(String userMessage, String userContext) {
        String systemPrompt = "You are FinFlare, an AI financial assistant. " +
                "Help users with personal finance questions, expense analysis, budgeting advice, " +
                "and financial planning. Be concise, helpful, and encouraging. " +
                "User context: " + (userContext != null ? userContext : "No specific context provided.");

        Map<String, Object> requestBody = createChatCompletionRequest(
            systemPrompt + "\n\nUser: " + userMessage, 
            200
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::extractMessageContent)
                .onErrorReturn("I apologize, but I'm unable to process your request right now. Please try again later.");
    }

    public Mono<String> analyzeSpendingPattern(List<Map<String, Object>> expenses) {
        StringBuilder expenseData = new StringBuilder();
        expenseData.append("Analyze these recent expenses and provide insights:\n");
        
        for (Map<String, Object> expense : expenses) {
            expenseData.append(String.format("- %s: $%s (%s)\n", 
                expense.get("description"), 
                expense.get("amount"), 
                expense.get("category")));
        }

        expenseData.append("\nProvide insights on spending patterns, suggestions for improvement, and potential budget optimizations. Keep it concise and actionable.");

        Map<String, Object> requestBody = createChatCompletionRequest(expenseData.toString(), 300);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::extractMessageContent)
                .onErrorReturn("Unable to analyze spending patterns at this time.");
    }

    public Mono<String> generateBudgetAdvice(double totalIncome, Map<String, Double> categorySpending) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Based on the following financial data, provide budget advice:\n");
        prompt.append(String.format("Monthly Income: $%.2f\n", totalIncome));
        prompt.append("Category Spending:\n");
        
        categorySpending.forEach((category, amount) -> 
            prompt.append(String.format("- %s: $%.2f\n", category, amount)));
        
        prompt.append("\nProvide specific budget recommendations, savings opportunities, and financial tips. Be practical and actionable.");

        Map<String, Object> requestBody = createChatCompletionRequest(prompt.toString(), 250);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::extractMessageContent)
                .onErrorReturn("Unable to generate budget advice at this time.");
    }

    private Map<String, Object> createChatCompletionRequest(String prompt, int maxTokens) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 0.7);

        return requestBody;
    }

    @SuppressWarnings("unchecked")
    private String extractMessageContent(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Log error and return fallback
            System.err.println("Error extracting AI response: " + e.getMessage());
        }
        return "Unable to process request";
    }
}
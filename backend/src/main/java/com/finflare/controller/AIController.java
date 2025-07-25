package com.finflare.controller;

import com.finflare.dto.ChatRequest;
import com.finflare.dto.ChatResponse;
import com.finflare.dto.ExpenseCategorizeRequest;
import com.finflare.security.UserPrincipal;
import com.finflare.service.AIService;
import com.finflare.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI Features", description = "AI-powered features for financial assistance")
public class AIController {

    @Autowired
    private AIService aiService;

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/chat")
    @Operation(summary = "Chat with AI financial assistant")
    public Mono<ResponseEntity<ChatResponse>> chat(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        // Get user context for better AI responses
        String userContext = String.format("User ID: %d", userPrincipal.getId());
        
        return aiService.getChatbotResponse(request.getMessage(), userContext)
                .map(response -> ResponseEntity.ok(new ChatResponse(response)));
    }

    @PostMapping("/categorize")
    @Operation(summary = "Auto-categorize expense using AI")
    public Mono<ResponseEntity<Map<String, String>>> categorizeExpense(
            @Valid @RequestBody ExpenseCategorizeRequest request) {
        
        return aiService.categorizeExpense(request.getDescription(), request.getAmount())
                .map(category -> ResponseEntity.ok(Map.of("category", category)));
    }

    @GetMapping("/spending-analysis")
    @Operation(summary = "Get AI analysis of spending patterns")
    public Mono<ResponseEntity<ChatResponse>> analyzeSpending(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "30") int days) {
        
        List<Map<String, Object>> recentExpenses = expenseService.getRecentExpensesForAnalysis(
                userPrincipal.getId(), days);
        
        return aiService.analyzeSpendingPattern(recentExpenses)
                .map(analysis -> ResponseEntity.ok(new ChatResponse(analysis)));
    }

    @GetMapping("/budget-advice")
    @Operation(summary = "Get AI budget advice")
    public Mono<ResponseEntity<ChatResponse>> getBudgetAdvice(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false, defaultValue = "0") double monthlyIncome) {
        
        Map<String, Double> categorySpending = expenseService.getCategorySpendingForCurrentMonth(
                userPrincipal.getId());
        
        return aiService.generateBudgetAdvice(monthlyIncome, categorySpending)
                .map(advice -> ResponseEntity.ok(new ChatResponse(advice)));
    }

    @PostMapping("/financial-goals")
    @Operation(summary = "Get AI recommendations for financial goals")
    public Mono<ResponseEntity<ChatResponse>> getFinancialGoalAdvice(
            @RequestBody Map<String, Object> goalData,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        String goalType = (String) goalData.get("goalType");
        Double targetAmount = ((Number) goalData.get("targetAmount")).doubleValue();
        Integer timeframeMonths = ((Number) goalData.get("timeframeMonths")).intValue();
        
        String prompt = String.format(
            "Help me create a plan to achieve my %s goal of $%.2f in %d months. " +
            "Provide specific, actionable steps and budgeting advice.",
            goalType, targetAmount, timeframeMonths
        );
        
        return aiService.getChatbotResponse(prompt, "Financial goal planning")
                .map(advice -> ResponseEntity.ok(new ChatResponse(advice)));
    }
}
package com.finflare.controller;

import com.finflare.dto.BudgetCreateRequest;
import com.finflare.dto.BudgetResponse;
import com.finflare.dto.BudgetUpdateRequest;
import com.finflare.model.Budget;
import com.finflare.model.ExpenseCategory;
import com.finflare.security.UserPrincipal;
import com.finflare.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@Tag(name = "Budget Management", description = "APIs for managing budgets and tracking spending")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    @Operation(summary = "Create a new budget")
    public ResponseEntity<BudgetResponse> createBudget(
            @Valid @RequestBody BudgetCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Budget budget = budgetService.createBudget(request, userPrincipal.getId());
        return ResponseEntity.ok(BudgetResponse.fromBudget(budget));
    }

    @GetMapping
    @Operation(summary = "Get all budgets for the authenticated user")
    public ResponseEntity<Page<BudgetResponse>> getAllBudgets(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Pageable pageable) {
        Page<Budget> budgets = budgetService.getBudgetsByUserId(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(budgets.map(BudgetResponse::fromBudget));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active budgets for the authenticated user")
    public ResponseEntity<List<BudgetResponse>> getActiveBudgets(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Budget> budgets = budgetService.getActiveBudgetsByUserId(userPrincipal.getId());
        return ResponseEntity.ok(budgets.stream().map(BudgetResponse::fromBudget).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get budget by ID")
    public ResponseEntity<BudgetResponse> getBudgetById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Budget budget = budgetService.getBudgetById(id, userPrincipal.getId());
        return ResponseEntity.ok(BudgetResponse.fromBudget(budget));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get budgets by category")
    public ResponseEntity<List<BudgetResponse>> getBudgetsByCategory(
            @PathVariable ExpenseCategory category,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Budget> budgets = budgetService.getBudgetsByCategory(category, userPrincipal.getId());
        return ResponseEntity.ok(budgets.stream().map(BudgetResponse::fromBudget).toList());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update budget")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Budget budget = budgetService.updateBudget(id, request, userPrincipal.getId());
        return ResponseEntity.ok(BudgetResponse.fromBudget(budget));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete budget")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        budgetService.deleteBudget(id, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/toggle-alert")
    @Operation(summary = "Toggle budget alert")
    public ResponseEntity<BudgetResponse> toggleBudgetAlert(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Budget budget = budgetService.toggleBudgetAlert(id, userPrincipal.getId());
        return ResponseEntity.ok(BudgetResponse.fromBudget(budget));
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get budget alerts for the authenticated user")
    public ResponseEntity<List<BudgetResponse>> getBudgetAlerts(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Budget> budgets = budgetService.getBudgetAlerts(userPrincipal.getId());
        return ResponseEntity.ok(budgets.stream().map(BudgetResponse::fromBudget).toList());
    }

    @PostMapping("/{id}/reset")
    @Operation(summary = "Reset budget spent amount")
    public ResponseEntity<BudgetResponse> resetBudget(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Budget budget = budgetService.resetBudget(id, userPrincipal.getId());
        return ResponseEntity.ok(BudgetResponse.fromBudget(budget));
    }
}
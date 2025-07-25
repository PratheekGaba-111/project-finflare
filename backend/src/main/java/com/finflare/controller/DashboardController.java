package com.finflare.controller;

import com.finflare.dto.DashboardResponse;
import com.finflare.security.UserPrincipal;
import com.finflare.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Dashboard analytics and summary data")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get comprehensive dashboard data")
    public ResponseEntity<DashboardResponse> getDashboardData(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        DashboardResponse dashboard = dashboardService.getDashboardData(userPrincipal.getId());
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/spending-summary")
    @Operation(summary = "Get spending summary for a specific period")
    public ResponseEntity<Map<String, Object>> getSpendingSummary(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1); // First day of current month
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        Map<String, Object> summary = dashboardService.getSpendingSummary(
                userPrincipal.getId(), startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/category-breakdown")
    @Operation(summary = "Get expense breakdown by category")
    public ResponseEntity<Map<String, Object>> getCategoryBreakdown(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "30") int days) {
        
        Map<String, Object> breakdown = dashboardService.getCategoryBreakdown(
                userPrincipal.getId(), days);
        return ResponseEntity.ok(breakdown);
    }

    @GetMapping("/spending-trends")
    @Operation(summary = "Get spending trends over time")
    public ResponseEntity<Map<String, Object>> getSpendingTrends(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "12") int months) {
        
        Map<String, Object> trends = dashboardService.getSpendingTrends(
                userPrincipal.getId(), months);
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/budget-progress")
    @Operation(summary = "Get budget progress for all active budgets")
    public ResponseEntity<Map<String, Object>> getBudgetProgress(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Map<String, Object> progress = dashboardService.getBudgetProgress(userPrincipal.getId());
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/financial-health")
    @Operation(summary = "Get financial health score and insights")
    public ResponseEntity<Map<String, Object>> getFinancialHealth(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Map<String, Object> health = dashboardService.getFinancialHealthScore(userPrincipal.getId());
        return ResponseEntity.ok(health);
    }

    @GetMapping("/recent-activity")
    @Operation(summary = "Get recent financial activity")
    public ResponseEntity<Map<String, Object>> getRecentActivity(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "10") int limit) {
        
        Map<String, Object> activity = dashboardService.getRecentActivity(
                userPrincipal.getId(), limit);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/savings-insights")
    @Operation(summary = "Get savings opportunities and insights")
    public ResponseEntity<Map<String, Object>> getSavingsInsights(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Map<String, Object> insights = dashboardService.getSavingsInsights(userPrincipal.getId());
        return ResponseEntity.ok(insights);
    }
}
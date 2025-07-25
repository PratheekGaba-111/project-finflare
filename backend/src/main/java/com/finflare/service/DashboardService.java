package com.finflare.service;

import com.finflare.dto.BudgetResponse;
import com.finflare.dto.DashboardResponse;
import com.finflare.model.Budget;
import com.finflare.model.Expense;
import com.finflare.model.ExpenseCategory;
import com.finflare.repository.BudgetRepository;
import com.finflare.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetService budgetService;

    public DashboardResponse getDashboardData(Long userId) {
        DashboardResponse dashboard = new DashboardResponse();

        // Calculate key metrics
        dashboard.setMonthlyExpenses(getMonthlyExpenses(userId));
        dashboard.setCategorySpending(getCategorySpendingForCurrentMonth(userId));
        dashboard.setRecentTransactions(getRecentActivity(userId, 10));
        dashboard.setBudgetProgress(getBudgetProgress(userId));
        dashboard.setSpendingTrends(getSpendingTrends(userId, 12));
        dashboard.setFinancialHealthScore(calculateFinancialHealthScore(userId));
        dashboard.setInsights(generateInsights(userId));
        dashboard.setBudgetAlerts(getBudgetAlerts(userId));
        dashboard.setSavingsGoals(getSavingsInsights(userId));

        // Calculate savings (simplified - would need income data in real app)
        BigDecimal monthlyExpenses = dashboard.getMonthlyExpenses();
        BigDecimal estimatedSavings = monthlyExpenses.multiply(BigDecimal.valueOf(-0.1)); // Placeholder
        dashboard.setTotalSavings(estimatedSavings);

        return dashboard;
    }

    public Map<String, Object> getSpendingSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
        
        BigDecimal totalSpent = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> categoryBreakdown = expenses.stream()
                .collect(Collectors.groupingBy(
                    expense -> expense.getCategory().name(),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSpent", totalSpent);
        summary.put("categoryBreakdown", categoryBreakdown);
        summary.put("transactionCount", expenses.size());
        summary.put("averageTransaction", 
                expenses.isEmpty() ? BigDecimal.ZERO : 
                totalSpent.divide(BigDecimal.valueOf(expenses.size()), 2, RoundingMode.HALF_UP));
        summary.put("period", Map.of("startDate", startDate, "endDate", endDate));

        return summary;
    }

    public Map<String, Object> getCategoryBreakdown(Long userId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);

        Map<String, BigDecimal> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                    expense -> expense.getCategory().getDisplayName(),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        BigDecimal totalSpent = categoryTotals.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Double> categoryPercentages = categoryTotals.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> totalSpent.compareTo(BigDecimal.ZERO) == 0 ? 0.0 :
                            entry.getValue().divide(totalSpent, 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100)).doubleValue()
                ));

        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("categoryTotals", categoryTotals);
        breakdown.put("categoryPercentages", categoryPercentages);
        breakdown.put("totalSpent", totalSpent);
        breakdown.put("period", days + " days");

        return breakdown;
    }

    public Map<String, Object> getSpendingTrends(Long userId, int months) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);

        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);

        Map<String, BigDecimal> monthlyTrends = expenses.stream()
                .collect(Collectors.groupingBy(
                    expense -> expense.getExpenseDate().getYear() + "-" + 
                              String.format("%02d", expense.getExpenseDate().getMonthValue()),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        List<Map<String, Object>> trendData = monthlyTrends.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> Map.of("month", entry.getKey(), "amount", entry.getValue()))
                .collect(Collectors.toList());

        Map<String, Object> trends = new HashMap<>();
        trends.put("monthlyData", trendData);
        trends.put("averageMonthlySpending", 
                monthlyTrends.values().stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(Math.max(1, monthlyTrends.size())), 2, RoundingMode.HALF_UP));

        return trends;
    }

    public Map<String, Object> getBudgetProgress(Long userId) {
        List<Budget> activeBudgets = budgetRepository.findByUserIdAndIsActiveTrue(userId);

        List<Map<String, Object>> budgetProgress = activeBudgets.stream()
                .map(budget -> {
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("category", budget.getCategory().getDisplayName());
                    progress.put("budgetAmount", budget.getBudgetAmount());
                    progress.put("spentAmount", budget.getSpentAmount());
                    progress.put("spentPercentage", budget.getSpentPercentage());
                    progress.put("remainingAmount", budget.getRemainingAmount());
                    progress.put("isOverBudget", budget.isOverBudget());
                    progress.put("shouldAlert", budget.shouldAlert());
                    return progress;
                })
                .collect(Collectors.toList());

        BigDecimal totalBudget = activeBudgets.stream()
                .map(Budget::getBudgetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = activeBudgets.stream()
                .map(Budget::getSpentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> summary = new HashMap<>();
        summary.put("budgets", budgetProgress);
        summary.put("totalBudget", totalBudget);
        summary.put("totalSpent", totalSpent);
        summary.put("overallProgress", 
                totalBudget.compareTo(BigDecimal.ZERO) == 0 ? 0.0 :
                totalSpent.divide(totalBudget, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).doubleValue());

        return summary;
    }

    public Map<String, Object> getFinancialHealthScore(Long userId) {
        int score = calculateFinancialHealthScore(userId);
        
        Map<String, Object> health = new HashMap<>();
        health.put("score", score);
        health.put("rating", getHealthRating(score));
        health.put("recommendations", getHealthRecommendations(score));
        
        return health;
    }

    public Map<String, Object> getRecentActivity(Long userId, int limit) {
        List<Expense> recentExpenses = expenseRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        
        List<Map<String, Object>> activities = recentExpenses.stream()
                .limit(limit)
                .map(expense -> {
                    Map<String, Object> activity = new HashMap<>();
                    activity.put("id", expense.getId());
                    activity.put("description", expense.getDescription());
                    activity.put("amount", expense.getAmount());
                    activity.put("category", expense.getCategory().getDisplayName());
                    activity.put("date", expense.getExpenseDate());
                    activity.put("type", "expense");
                    return activity;
                })
                .collect(Collectors.toList());

        Map<String, Object> activity = new HashMap<>();
        activity.put("activities", activities);
        activity.put("totalActivities", activities.size());

        return activity;
    }

    public Map<String, Object> getSavingsInsights(Long userId) {
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastMonth = currentMonth.minusMonths(1);

        BigDecimal currentMonthSpending = getMonthlyExpenses(userId);
        BigDecimal lastMonthSpending = getSpendingForMonth(userId, lastMonth);

        BigDecimal savingsOpportunity = lastMonthSpending.subtract(currentMonthSpending);
        
        Map<String, Object> insights = new HashMap<>();
        insights.put("currentMonthSpending", currentMonthSpending);
        insights.put("lastMonthSpending", lastMonthSpending);
        insights.put("savingsOpportunity", savingsOpportunity);
        insights.put("recommendations", generateSavingsRecommendations(userId));

        return insights;
    }

    private BigDecimal getMonthlyExpenses(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();

        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, startOfMonth, endOfMonth)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> getCategorySpendingForCurrentMonth(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();

        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, startOfMonth, endOfMonth)
                .stream()
                .collect(Collectors.groupingBy(
                    expense -> expense.getCategory().getDisplayName(),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
    }

    private BigDecimal getSpendingForMonth(Long userId, LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());

        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, startOfMonth, endOfMonth)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Integer calculateFinancialHealthScore(Long userId) {
        int score = 100;

        // Check budget adherence
        List<Budget> activeBudgets = budgetRepository.findByUserIdAndIsActiveTrue(userId);
        long overBudgetCount = activeBudgets.stream().filter(Budget::isOverBudget).count();
        score -= (int) (overBudgetCount * 15);

        // Check spending consistency
        BigDecimal currentMonth = getMonthlyExpenses(userId);
        BigDecimal lastMonth = getSpendingForMonth(userId, LocalDate.now().minusMonths(1));
        
        if (lastMonth.compareTo(BigDecimal.ZERO) > 0 && currentMonth.compareTo(lastMonth.multiply(BigDecimal.valueOf(1.2))) > 0) {
            score -= 20; // Penalty for high spending increase
        }

        return Math.max(0, Math.min(100, score));
    }

    private String getHealthRating(int score) {
        if (score >= 80) return "Excellent";
        if (score >= 60) return "Good";
        if (score >= 40) return "Fair";
        return "Needs Improvement";
    }

    private List<String> getHealthRecommendations(int score) {
        List<String> recommendations = new ArrayList<>();
        
        if (score < 60) {
            recommendations.add("Review your budget and try to reduce unnecessary expenses");
            recommendations.add("Set up automatic savings to build an emergency fund");
        }
        if (score < 80) {
            recommendations.add("Consider tracking your expenses more closely");
            recommendations.add("Look for subscription services you can cancel");
        }
        
        recommendations.add("Keep up the good work with your financial planning!");
        return recommendations;
    }

    private List<String> generateInsights(Long userId) {
        List<String> insights = new ArrayList<>();
        
        Map<String, BigDecimal> categorySpending = getCategorySpendingForCurrentMonth(userId);
        if (!categorySpending.isEmpty()) {
            String topCategory = categorySpending.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Unknown");
            insights.add("Your highest spending category this month is " + topCategory);
        }

        List<Budget> alerts = budgetService.getBudgetAlerts(userId);
        if (!alerts.isEmpty()) {
            insights.add("You have " + alerts.size() + " budget alerts that need attention");
        }

        return insights;
    }

    private List<BudgetResponse> getBudgetAlerts(Long userId) {
        return budgetService.getBudgetAlerts(userId).stream()
                .map(BudgetResponse::fromBudget)
                .collect(Collectors.toList());
    }

    private List<String> generateSavingsRecommendations(Long userId) {
        List<String> recommendations = new ArrayList<>();
        
        Map<String, BigDecimal> categorySpending = getCategorySpendingForCurrentMonth(userId);
        
        // Find highest spending category and suggest reduction
        categorySpending.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> 
                    recommendations.add("Consider reducing spending in " + entry.getKey() + 
                                     " by 10% to save $" + entry.getValue().multiply(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.HALF_UP)));

        recommendations.add("Try the 50/30/20 rule: 50% needs, 30% wants, 20% savings");
        recommendations.add("Review subscriptions and cancel unused services");

        return recommendations;
    }
}
package com.finflare.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private BigDecimal totalBalance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal totalSavings;
    private Map<String, BigDecimal> categorySpending;
    private List<Map<String, Object>> recentTransactions;
    private Map<String, Object> budgetProgress;
    private Map<String, Object> spendingTrends;
    private Integer financialHealthScore;
    private List<String> insights;
    private List<BudgetResponse> budgetAlerts;
    private Map<String, Object> savingsGoals;
    private LocalDate lastUpdated;

    // Constructors
    public DashboardResponse() {
        this.lastUpdated = LocalDate.now();
    }

    // Getters and Setters
    public BigDecimal getTotalBalance() { return totalBalance; }
    public void setTotalBalance(BigDecimal totalBalance) { this.totalBalance = totalBalance; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public BigDecimal getTotalSavings() { return totalSavings; }
    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; }

    public Map<String, BigDecimal> getCategorySpending() { return categorySpending; }
    public void setCategorySpending(Map<String, BigDecimal> categorySpending) { this.categorySpending = categorySpending; }

    public List<Map<String, Object>> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<Map<String, Object>> recentTransactions) { this.recentTransactions = recentTransactions; }

    public Map<String, Object> getBudgetProgress() { return budgetProgress; }
    public void setBudgetProgress(Map<String, Object> budgetProgress) { this.budgetProgress = budgetProgress; }

    public Map<String, Object> getSpendingTrends() { return spendingTrends; }
    public void setSpendingTrends(Map<String, Object> spendingTrends) { this.spendingTrends = spendingTrends; }

    public Integer getFinancialHealthScore() { return financialHealthScore; }
    public void setFinancialHealthScore(Integer financialHealthScore) { this.financialHealthScore = financialHealthScore; }

    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }

    public List<BudgetResponse> getBudgetAlerts() { return budgetAlerts; }
    public void setBudgetAlerts(List<BudgetResponse> budgetAlerts) { this.budgetAlerts = budgetAlerts; }

    public Map<String, Object> getSavingsGoals() { return savingsGoals; }
    public void setSavingsGoals(Map<String, Object> savingsGoals) { this.savingsGoals = savingsGoals; }

    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }
}
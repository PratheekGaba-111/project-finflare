package com.finflare.dto;

import com.finflare.model.Budget;
import com.finflare.model.BudgetPeriod;
import com.finflare.model.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BudgetResponse {
    private Long id;
    private ExpenseCategory category;
    private BigDecimal budgetAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Double spentPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private BudgetPeriod period;
    private boolean alertEnabled;
    private Integer alertThreshold;
    private boolean isActive;
    private boolean isOverBudget;
    private boolean shouldAlert;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public BudgetResponse() {}

    // Static factory method
    public static BudgetResponse fromBudget(Budget budget) {
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setCategory(budget.getCategory());
        response.setBudgetAmount(budget.getBudgetAmount());
        response.setSpentAmount(budget.getSpentAmount());
        response.setRemainingAmount(budget.getRemainingAmount());
        response.setSpentPercentage(budget.getSpentPercentage());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());
        response.setPeriod(budget.getPeriod());
        response.setAlertEnabled(budget.isAlertEnabled());
        response.setAlertThreshold(budget.getAlertThreshold());
        response.setActive(budget.isActive());
        response.setOverBudget(budget.isOverBudget());
        response.setShouldAlert(budget.shouldAlert());
        response.setCreatedAt(budget.getCreatedAt());
        response.setUpdatedAt(budget.getUpdatedAt());
        return response;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public BigDecimal getSpentAmount() { return spentAmount; }
    public void setSpentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; }

    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }

    public Double getSpentPercentage() { return spentPercentage; }
    public void setSpentPercentage(Double spentPercentage) { this.spentPercentage = spentPercentage; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BudgetPeriod getPeriod() { return period; }
    public void setPeriod(BudgetPeriod period) { this.period = period; }

    public boolean isAlertEnabled() { return alertEnabled; }
    public void setAlertEnabled(boolean alertEnabled) { this.alertEnabled = alertEnabled; }

    public Integer getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(Integer alertThreshold) { this.alertThreshold = alertThreshold; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isOverBudget() { return isOverBudget; }
    public void setOverBudget(boolean overBudget) { isOverBudget = overBudget; }

    public boolean isShouldAlert() { return shouldAlert; }
    public void setShouldAlert(boolean shouldAlert) { this.shouldAlert = shouldAlert; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
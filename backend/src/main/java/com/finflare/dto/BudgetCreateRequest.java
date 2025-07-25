package com.finflare.dto;

import com.finflare.model.BudgetPeriod;
import com.finflare.model.ExpenseCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetCreateRequest {
    @NotNull
    private ExpenseCategory category;

    @NotNull
    @Positive
    private BigDecimal budgetAmount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private BudgetPeriod period = BudgetPeriod.MONTHLY;

    private boolean alertEnabled = true;

    private Integer alertThreshold = 80;

    // Constructors
    public BudgetCreateRequest() {}

    // Getters and Setters
    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

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
}
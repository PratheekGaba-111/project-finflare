package com.finflare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ExpenseCategory category;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal budgetAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BudgetPeriod period = BudgetPeriod.MONTHLY;

    private boolean alertEnabled = true;

    private Integer alertThreshold = 80; // Alert when 80% of budget is reached

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Constructors
    public Budget() {}

    public Budget(ExpenseCategory category, BigDecimal budgetAmount, LocalDate startDate, LocalDate endDate, User user) {
        this.category = category;
        this.budgetAmount = budgetAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    // Utility methods
    public BigDecimal getRemainingAmount() {
        return budgetAmount.subtract(spentAmount);
    }

    public Double getSpentPercentage() {
        if (budgetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return spentAmount.divide(budgetAmount, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    public boolean isOverBudget() {
        return spentAmount.compareTo(budgetAmount) > 0;
    }

    public boolean shouldAlert() {
        return alertEnabled && getSpentPercentage() >= alertThreshold;
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

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

enum BudgetPeriod {
    WEEKLY, MONTHLY, QUARTERLY, YEARLY
}
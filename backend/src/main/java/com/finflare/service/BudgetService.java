package com.finflare.service;

import com.finflare.model.Budget;
import com.finflare.model.ExpenseCategory;
import com.finflare.model.User;
import com.finflare.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget createBudget(Budget budget) {
        // Deactivate existing budget for the same category if active
        Optional<Budget> existingBudget = budgetRepository.findByUserAndCategoryAndIsActive(
            budget.getUser(), budget.getCategory(), true);
        
        if (existingBudget.isPresent()) {
            Budget existing = existingBudget.get();
            existing.setActive(false);
            budgetRepository.save(existing);
        }

        return budgetRepository.save(budget);
    }

    public List<Budget> getUserBudgets(User user) {
        return budgetRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Budget> getActiveBudgets(User user) {
        return budgetRepository.findByUserAndIsActiveOrderByCreatedAtDesc(user, true);
    }

    public Optional<Budget> getActiveBudgetByCategory(User user, ExpenseCategory category, LocalDate date) {
        return budgetRepository.findActiveBudgetByUserCategoryAndDate(user, category, date);
    }

    public Budget updateBudget(Long budgetId, Budget updatedBudget, User user) {
        Optional<Budget> existingBudgetOpt = budgetRepository.findById(budgetId);
        if (existingBudgetOpt.isPresent()) {
            Budget existingBudget = existingBudgetOpt.get();
            if (existingBudget.getUser().getId().equals(user.getId())) {
                existingBudget.setBudgetAmount(updatedBudget.getBudgetAmount());
                existingBudget.setStartDate(updatedBudget.getStartDate());
                existingBudget.setEndDate(updatedBudget.getEndDate());
                existingBudget.setAlertEnabled(updatedBudget.isAlertEnabled());
                existingBudget.setAlertThreshold(updatedBudget.getAlertThreshold());
                existingBudget.setActive(updatedBudget.isActive());

                return budgetRepository.save(existingBudget);
            }
        }
        return null;
    }

    public boolean deleteBudget(Long budgetId, User user) {
        Optional<Budget> budgetOpt = budgetRepository.findById(budgetId);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            if (budget.getUser().getId().equals(user.getId())) {
                budgetRepository.delete(budget);
                return true;
            }
        }
        return false;
    }

    public void updateBudgetSpending(User user, ExpenseCategory category, BigDecimal amount, LocalDate expenseDate) {
        Optional<Budget> budgetOpt = getActiveBudgetByCategory(user, category, expenseDate);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            budget.setSpentAmount(budget.getSpentAmount().add(amount));
            budgetRepository.save(budget);
        }
    }

    public List<Budget> getBudgetsExceedingThreshold() {
        return budgetRepository.findBudgetsExceedingThreshold();
    }

    public List<Budget> getBudgetsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return budgetRepository.findByUserAndStartDateBetween(user, startDate, endDate);
    }
}
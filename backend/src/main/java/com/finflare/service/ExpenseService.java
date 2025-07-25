package com.finflare.service;

import com.finflare.model.Expense;
import com.finflare.model.ExpenseCategory;
import com.finflare.model.User;
import com.finflare.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private GamificationService gamificationService;

    @Value("${app.ml.service.url}")
    private String mlServiceUrl;

    private final WebClient webClient = WebClient.builder().build();

    public Expense createExpense(Expense expense) {
        // Auto-categorize using AI if description is provided
        if (expense.getDescription() != null && !expense.getDescription().trim().isEmpty()) {
            categorizeExpenseWithAI(expense);
        }

        Expense savedExpense = expenseRepository.save(expense);

        // Update budget if applicable
        budgetService.updateBudgetSpending(expense.getUser(), expense.getCategory(), expense.getAmount(), expense.getExpenseDate());

        // Update gamification
        gamificationService.handleExpenseAdded(expense.getUser(), expense.getExpenseDate());

        return savedExpense;
    }

    public List<Expense> getUserExpenses(User user) {
        return expenseRepository.findByUserOrderByExpenseDateDesc(user);
    }

    public List<Expense> getUserExpensesByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserAndExpenseDateBetweenOrderByExpenseDateDesc(user, startDate, endDate);
    }

    public List<Expense> getUserExpensesByCategory(User user, ExpenseCategory category) {
        return expenseRepository.findByUserAndCategoryOrderByExpenseDateDesc(user, category);
    }

    public BigDecimal getTotalExpensesByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = expenseRepository.getTotalExpensesByUserAndDateRange(user, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Map<ExpenseCategory, BigDecimal> getCategoryWiseExpenses(User user, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = expenseRepository.getCategoryWiseExpensesByUserAndDateRange(user, startDate, endDate);
        Map<ExpenseCategory, BigDecimal> categoryExpenses = new HashMap<>();

        for (Object[] result : results) {
            ExpenseCategory category = (ExpenseCategory) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            categoryExpenses.put(category, amount);
        }

        return categoryExpenses;
    }

    public Map<String, Object> getMonthlyReport(User user, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        Map<String, Object> report = new HashMap<>();
        report.put("month", month.toString());
        report.put("totalExpenses", getTotalExpensesByDateRange(user, startDate, endDate));
        report.put("categoryWiseExpenses", getCategoryWiseExpenses(user, startDate, endDate));
        report.put("expensesList", getUserExpensesByDateRange(user, startDate, endDate));

        // Get previous month for comparison
        YearMonth previousMonth = month.minusMonths(1);
        LocalDate prevStartDate = previousMonth.atDay(1);
        LocalDate prevEndDate = previousMonth.atEndOfMonth();
        BigDecimal previousMonthTotal = getTotalExpensesByDateRange(user, prevStartDate, prevEndDate);

        report.put("previousMonthTotal", previousMonthTotal);
        report.put("monthlyChange", calculatePercentageChange(previousMonthTotal, (BigDecimal) report.get("totalExpenses")));

        return report;
    }

    public Expense updateExpense(Long expenseId, Expense updatedExpense, User user) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findById(expenseId);
        if (existingExpenseOpt.isPresent()) {
            Expense existingExpense = existingExpenseOpt.get();
            if (existingExpense.getUser().getId().equals(user.getId())) {
                // Update the budget (subtract old amount, add new amount)
                budgetService.updateBudgetSpending(user, existingExpense.getCategory(), 
                    existingExpense.getAmount().negate(), existingExpense.getExpenseDate());
                
                existingExpense.setAmount(updatedExpense.getAmount());
                existingExpense.setDescription(updatedExpense.getDescription());
                existingExpense.setCategory(updatedExpense.getCategory());
                existingExpense.setExpenseDate(updatedExpense.getExpenseDate());
                existingExpense.setNotes(updatedExpense.getNotes());

                Expense savedExpense = expenseRepository.save(existingExpense);

                // Update budget with new amount
                budgetService.updateBudgetSpending(user, savedExpense.getCategory(), 
                    savedExpense.getAmount(), savedExpense.getExpenseDate());

                return savedExpense;
            }
        }
        return null;
    }

    public boolean deleteExpense(Long expenseId, User user) {
        Optional<Expense> expenseOpt = expenseRepository.findById(expenseId);
        if (expenseOpt.isPresent()) {
            Expense expense = expenseOpt.get();
            if (expense.getUser().getId().equals(user.getId())) {
                // Update budget (subtract the deleted expense amount)
                budgetService.updateBudgetSpending(user, expense.getCategory(), 
                    expense.getAmount().negate(), expense.getExpenseDate());
                
                expenseRepository.delete(expense);
                return true;
            }
        }
        return false;
    }

    private void categorizeExpenseWithAI(Expense expense) {
        try {
            Map<String, String> request = new HashMap<>();
            request.put("description", expense.getDescription());

            Mono<Map> response = webClient.post()
                    .uri(mlServiceUrl + "/categorize")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, Object> result = response.block();
            if (result != null && result.containsKey("category")) {
                String predictedCategory = (String) result.get("category");
                Double confidence = result.containsKey("confidence") ? 
                    ((Number) result.get("confidence")).doubleValue() : 0.0;

                expense.setOriginalCategory(expense.getCategory() != null ? 
                    expense.getCategory().name() : null);
                
                try {
                    ExpenseCategory aiCategory = ExpenseCategory.valueOf(predictedCategory.toUpperCase());
                    expense.setCategory(aiCategory);
                    expense.setAiCategorized(true);
                    expense.setClassificationConfidence(confidence);
                } catch (IllegalArgumentException e) {
                    // If AI category doesn't match our enum, keep original or set to OTHER
                    if (expense.getCategory() == null) {
                        expense.setCategory(ExpenseCategory.OTHER);
                    }
                }
            }
        } catch (Exception e) {
            // If AI service is not available, keep the original category or set to OTHER
            if (expense.getCategory() == null) {
                expense.setCategory(ExpenseCategory.OTHER);
            }
        }
    }

    private Double calculatePercentageChange(BigDecimal oldValue, BigDecimal newValue) {
        if (oldValue.compareTo(BigDecimal.ZERO) == 0) {
            return newValue.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : 100.0;
        }
        return newValue.subtract(oldValue)
                .divide(oldValue, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    public List<Map<String, Object>> getRecentExpensesForAnalysis(Long userId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
        
        return expenses.stream()
                .map(expense -> {
                    Map<String, Object> expenseData = new HashMap<>();
                    expenseData.put("description", expense.getDescription());
                    expenseData.put("amount", expense.getAmount());
                    expenseData.put("category", expense.getCategory().getDisplayName());
                    expenseData.put("date", expense.getExpenseDate());
                    return expenseData;
                })
                .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
    }

    public Map<String, Double> getCategorySpendingForCurrentMonth(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();

        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startOfMonth, endOfMonth);
        
        return expenses.stream()
                .collect(HashMap::new,
                    (map, expense) -> map.merge(
                        expense.getCategory().getDisplayName(), 
                        expense.getAmount().doubleValue(), 
                        Double::sum),
                    (map1, map2) -> {
                        map2.forEach((key, value) -> map1.merge(key, value, Double::sum));
                        return map1;
                    });
    }
}
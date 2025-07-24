package com.finflare.controller;

import com.finflare.model.Expense;
import com.finflare.model.ExpenseCategory;
import com.finflare.model.User;
import com.finflare.repository.UserRepository;
import com.finflare.security.UserPrincipal;
import com.finflare.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getUserExpenses(Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<Expense> expenses = expenseService.getUserExpenses(user);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense, 
                                               Authentication authentication) {
        User user = getCurrentUser(authentication);
        expense.setUser(user);
        Expense savedExpense = expenseService.createExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, 
                                               @Valid @RequestBody Expense expense,
                                               Authentication authentication) {
        User user = getCurrentUser(authentication);
        Expense updatedExpense = expenseService.updateExpense(id, expense, user);
        if (updatedExpense != null) {
            return ResponseEntity.ok(updatedExpense);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, 
                                            Authentication authentication) {
        User user = getCurrentUser(authentication);
        boolean deleted = expenseService.deleteExpense(id, user);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(@PathVariable ExpenseCategory category,
                                                             Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<Expense> expenses = expenseService.getUserExpensesByCategory(user, category);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<Expense> expenses = expenseService.getUserExpensesByDateRange(user, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/reports/monthly/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(@PathVariable int year, 
                                                               @PathVariable int month,
                                                               Authentication authentication) {
        User user = getCurrentUser(authentication);
        YearMonth yearMonth = YearMonth.of(year, month);
        Map<String, Object> report = expenseService.getMonthlyReport(user, yearMonth);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/categories")
    public ResponseEntity<ExpenseCategory[]> getExpenseCategories() {
        return ResponseEntity.ok(ExpenseCategory.values());
    }
}
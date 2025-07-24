package com.finflare.repository;

import com.finflare.model.Expense;
import com.finflare.model.ExpenseCategory;
import com.finflare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    List<Expense> findByUserOrderByExpenseDateDesc(User user);
    
    List<Expense> findByUserAndExpenseDateBetweenOrderByExpenseDateDesc(
        User user, LocalDate startDate, LocalDate endDate);
    
    List<Expense> findByUserAndCategoryOrderByExpenseDateDesc(User user, ExpenseCategory category);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpensesByUserAndDateRange(
        @Param("user") User user, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.category = :category AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpensesByUserCategoryAndDateRange(
        @Param("user") User user, 
        @Param("category") ExpenseCategory category,
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate GROUP BY e.category")
    List<Object[]> getCategoryWiseExpensesByUserAndDateRange(
        @Param("user") User user, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(e) FROM Expense e WHERE e.user = :user AND e.expenseDate = :date")
    Long countExpensesByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT DISTINCT e.expenseDate FROM Expense e WHERE e.user = :user ORDER BY e.expenseDate DESC")
    List<LocalDate> getDistinctExpenseDatesByUser(@Param("user") User user);
    
    List<Expense> findByUserAndSourceOrderByCreatedAtDesc(User user, com.finflare.model.ExpenseSource source);
}
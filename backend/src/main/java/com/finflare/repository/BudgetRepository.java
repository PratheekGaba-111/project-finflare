package com.finflare.repository;

import com.finflare.model.Budget;
import com.finflare.model.ExpenseCategory;
import com.finflare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    List<Budget> findByUserOrderByCreatedAtDesc(User user);
    
    List<Budget> findByUserAndIsActiveOrderByCreatedAtDesc(User user, boolean isActive);
    
    Optional<Budget> findByUserAndCategoryAndIsActive(User user, ExpenseCategory category, boolean isActive);
    
    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.startDate <= :date AND b.endDate >= :date AND b.isActive = true")
    List<Budget> findActiveBudgetsByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.category = :category AND b.startDate <= :date AND b.endDate >= :date AND b.isActive = true")
    Optional<Budget> findActiveBudgetByUserCategoryAndDate(
        @Param("user") User user, 
        @Param("category") ExpenseCategory category, 
        @Param("date") LocalDate date);
    
    @Query("SELECT b FROM Budget b WHERE b.alertEnabled = true AND b.spentAmount >= (b.budgetAmount * b.alertThreshold / 100)")
    List<Budget> findBudgetsExceedingThreshold();
    
    List<Budget> findByUserAndStartDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
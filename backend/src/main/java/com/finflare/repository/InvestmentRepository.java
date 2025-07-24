package com.finflare.repository;

import com.finflare.model.Investment;
import com.finflare.model.InvestmentType;
import com.finflare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    
    List<Investment> findByUserOrderByPurchaseDateDesc(User user);
    
    List<Investment> findByUserAndTypeOrderByPurchaseDateDesc(User user, InvestmentType type);
    
    List<Investment> findByUserAndSymbolOrderByPurchaseDateDesc(User user, String symbol);
    
    @Query("SELECT SUM(i.purchasePrice * i.quantity) FROM Investment i WHERE i.user = :user")
    BigDecimal getTotalInvestmentByUser(@Param("user") User user);
    
    @Query("SELECT SUM(COALESCE(i.currentPrice, i.purchasePrice) * i.quantity) FROM Investment i WHERE i.user = :user")
    BigDecimal getCurrentPortfolioValueByUser(@Param("user") User user);
    
    @Query("SELECT i.type, SUM(i.purchasePrice * i.quantity) FROM Investment i WHERE i.user = :user GROUP BY i.type")
    List<Object[]> getInvestmentsByTypeAndUser(@Param("user") User user);
    
    @Query("SELECT i.symbol, SUM(i.quantity) FROM Investment i WHERE i.user = :user GROUP BY i.symbol")
    List<Object[]> getInvestmentsBySymbolAndUser(@Param("user") User user);
    
    @Query("SELECT COUNT(DISTINCT i.symbol) FROM Investment i WHERE i.user = :user")
    Long getPortfolioDiversificationByUser(@Param("user") User user);
}
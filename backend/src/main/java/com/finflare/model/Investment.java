package com.finflare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String symbol; // Stock symbol or crypto symbol

    @NotBlank
    private String name; // Full name of the asset

    @Enumerated(EnumType.STRING)
    @NotNull
    private InvestmentType type;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal currentPrice;

    @NotNull
    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private String sector; // For stocks

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Constructors
    public Investment() {}

    public Investment(String symbol, String name, InvestmentType type, Integer quantity, 
                     BigDecimal purchasePrice, User user) {
        this.symbol = symbol;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.user = user;
        this.purchaseDate = LocalDateTime.now();
    }

    // Utility methods
    public BigDecimal getTotalInvestment() {
        return purchasePrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getCurrentValue() {
        if (currentPrice == null) {
            return getTotalInvestment();
        }
        return currentPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getProfitLoss() {
        return getCurrentValue().subtract(getTotalInvestment());
    }

    public Double getProfitLossPercentage() {
        BigDecimal totalInvestment = getTotalInvestment();
        if (totalInvestment.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return getProfitLoss().divide(totalInvestment, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public InvestmentType getType() { return type; }
    public void setType(InvestmentType type) { this.type = type; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

enum InvestmentType {
    STOCK, CRYPTO, BOND, ETF, MUTUAL_FUND
}

enum RiskLevel {
    LOW, MEDIUM, HIGH, VERY_HIGH
}
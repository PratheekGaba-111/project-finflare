package com.finflare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank
    @Size(max = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ExpenseCategory category;

    @NotNull
    private LocalDate expenseDate;

    @Size(max = 500)
    private String notes;

    private String receiptImageUrl;

    @Enumerated(EnumType.STRING)
    private ExpenseSource source = ExpenseSource.MANUAL; // MANUAL, OCR, VOICE

    private boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // AI Classification confidence score (0-1)
    private Double classificationConfidence;

    private String originalCategory; // Before AI classification
    private boolean aiCategorized = false;

    // Constructors
    public Expense() {}

    public Expense(BigDecimal amount, String description, ExpenseCategory category, LocalDate expenseDate, User user) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.expenseDate = expenseDate;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getReceiptImageUrl() { return receiptImageUrl; }
    public void setReceiptImageUrl(String receiptImageUrl) { this.receiptImageUrl = receiptImageUrl; }

    public ExpenseSource getSource() { return source; }
    public void setSource(ExpenseSource source) { this.source = source; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public RecurrenceType getRecurrenceType() { return recurrenceType; }
    public void setRecurrenceType(RecurrenceType recurrenceType) { this.recurrenceType = recurrenceType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Double getClassificationConfidence() { return classificationConfidence; }
    public void setClassificationConfidence(Double classificationConfidence) { this.classificationConfidence = classificationConfidence; }

    public String getOriginalCategory() { return originalCategory; }
    public void setOriginalCategory(String originalCategory) { this.originalCategory = originalCategory; }

    public boolean isAiCategorized() { return aiCategorized; }
    public void setAiCategorized(boolean aiCategorized) { this.aiCategorized = aiCategorized; }
}

enum ExpenseCategory {
    FOOD_DINING("Food & Dining"),
    TRANSPORTATION("Transportation"),
    SHOPPING("Shopping"),
    ENTERTAINMENT("Entertainment"),
    BILLS_UTILITIES("Bills & Utilities"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    TRAVEL("Travel"),
    GROCERIES("Groceries"),
    INSURANCE("Insurance"),
    INVESTMENTS("Investments"),
    GIFTS_DONATIONS("Gifts & Donations"),
    PERSONAL_CARE("Personal Care"),
    HOME_GARDEN("Home & Garden"),
    BUSINESS("Business"),
    OTHER("Other");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

enum ExpenseSource {
    MANUAL, OCR, VOICE
}

enum RecurrenceType {
    DAILY, WEEKLY, MONTHLY, YEARLY
}
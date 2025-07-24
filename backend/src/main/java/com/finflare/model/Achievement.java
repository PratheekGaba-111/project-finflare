package com.finflare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AchievementType type;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer pointsAwarded;

    private String iconUrl;

    @NotNull
    private boolean isUnlocked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime unlockedAt;

    private LocalDateTime createdAt;

    // Constructors
    public Achievement() {}

    public Achievement(AchievementType type, String title, String description, Integer pointsAwarded, User user) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.pointsAwarded = pointsAwarded;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AchievementType getType() { return type; }
    public void setType(AchievementType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(Integer pointsAwarded) { this.pointsAwarded = pointsAwarded; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(LocalDateTime unlockedAt) { this.unlockedAt = unlockedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

enum AchievementType {
    FIRST_EXPENSE("First Expense"),
    WEEKLY_STREAK("Weekly Streak"),
    MONTHLY_STREAK("Monthly Streak"),
    BUDGET_KEEPER("Budget Keeper"),
    SAVINGS_MILESTONE("Savings Milestone"),
    INVESTMENT_STARTER("Investment Starter"),
    EXPENSE_TRACKER("Expense Tracker"),
    CATEGORY_MASTER("Category Master"),
    EARLY_BIRD("Early Bird"),
    NIGHT_OWL("Night Owl");

    private final String displayName;

    AchievementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
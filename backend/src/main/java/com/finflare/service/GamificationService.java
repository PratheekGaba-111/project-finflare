package com.finflare.service;

import com.finflare.model.Achievement;
import com.finflare.model.AchievementType;
import com.finflare.model.User;
import com.finflare.repository.AchievementRepository;
import com.finflare.repository.ExpenseRepository;
import com.finflare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class GamificationService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public void handleExpenseAdded(User user, LocalDate expenseDate) {
        updateStreak(user, expenseDate);
        checkAchievements(user);
        updateUserPoints(user);
    }

    private void updateStreak(User user, LocalDate expenseDate) {
        LocalDate today = LocalDate.now();
        LocalDate lastActivityDate = user.getLastActivityDate() != null ? 
            user.getLastActivityDate().toLocalDate() : null;

        if (lastActivityDate == null) {
            // First expense ever
            user.setCurrentStreak(1);
            user.setMaxStreak(1);
            unlockAchievement(user, AchievementType.FIRST_EXPENSE, "First Expense", 
                "Logged your first expense!", 10);
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastActivityDate, expenseDate);
            
            if (daysBetween == 1) {
                // Consecutive day
                user.setCurrentStreak(user.getCurrentStreak() + 1);
                if (user.getCurrentStreak() > user.getMaxStreak()) {
                    user.setMaxStreak(user.getCurrentStreak());
                }
            } else if (daysBetween > 1) {
                // Streak broken
                user.setCurrentStreak(1);
            }
            // If daysBetween == 0, it's the same day, don't update streak
        }

        user.setLastActivityDate(LocalDateTime.now());
        userRepository.save(user);

        // Check streak-based achievements
        if (user.getCurrentStreak() >= 7) {
            unlockAchievement(user, AchievementType.WEEKLY_STREAK, "Week Warrior", 
                "Logged expenses for 7 consecutive days!", 50);
        }
        if (user.getCurrentStreak() >= 30) {
            unlockAchievement(user, AchievementType.MONTHLY_STREAK, "Monthly Master", 
                "Logged expenses for 30 consecutive days!", 200);
        }
    }

    private void checkAchievements(User user) {
        // Check expense count achievements
        Long expenseCount = expenseRepository.countExpensesByUserAndDate(user, LocalDate.now());
        if (expenseCount >= 10) {
            unlockAchievement(user, AchievementType.EXPENSE_TRACKER, "Expense Tracker", 
                "Logged 10 expenses in a single day!", 25);
        }

        // Check category diversity
        List<LocalDate> expenseDates = expenseRepository.getDistinctExpenseDatesByUser(user);
        if (expenseDates.size() >= 30) {
            unlockAchievement(user, AchievementType.CATEGORY_MASTER, "Category Master", 
                "Tracked expenses across multiple categories!", 75);
        }
    }

    private void unlockAchievement(User user, AchievementType type, String title, String description, Integer points) {
        Optional<Achievement> existingAchievement = achievementRepository.findByUserAndType(user, type);
        
        if (existingAchievement.isEmpty()) {
            Achievement achievement = new Achievement(type, title, description, points, user);
            achievement.setUnlocked(true);
            achievement.setUnlockedAt(LocalDateTime.now());
            achievementRepository.save(achievement);

            // Update user points
            user.setTotalPoints(user.getTotalPoints() + points);
            userRepository.save(user);
        }
    }

    private void updateUserPoints(User user) {
        Integer achievementPoints = achievementRepository.getTotalPointsFromAchievementsByUser(user);
        if (achievementPoints != null) {
            user.setTotalPoints(achievementPoints);
            userRepository.save(user);
        }
    }

    public List<Achievement> getUserAchievements(User user) {
        return achievementRepository.findByUserOrderByUnlockedAtDesc(user);
    }

    public List<Achievement> getUnlockedAchievements(User user) {
        return achievementRepository.findByUserAndIsUnlockedOrderByUnlockedAtDesc(user, true);
    }

    public List<User> getLeaderboard() {
        return userRepository.findTopUsersByPoints();
    }

    public List<User> getStreakLeaderboard() {
        return userRepository.findTopUsersByStreak();
    }
}
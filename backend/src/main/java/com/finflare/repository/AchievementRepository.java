package com.finflare.repository;

import com.finflare.model.Achievement;
import com.finflare.model.AchievementType;
import com.finflare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    
    List<Achievement> findByUserOrderByUnlockedAtDesc(User user);
    
    List<Achievement> findByUserAndIsUnlockedOrderByUnlockedAtDesc(User user, boolean isUnlocked);
    
    Optional<Achievement> findByUserAndType(User user, AchievementType type);
    
    @Query("SELECT COUNT(a) FROM Achievement a WHERE a.user = :user AND a.isUnlocked = true")
    Long countUnlockedAchievementsByUser(@Param("user") User user);
    
    @Query("SELECT SUM(a.pointsAwarded) FROM Achievement a WHERE a.user = :user AND a.isUnlocked = true")
    Integer getTotalPointsFromAchievementsByUser(@Param("user") User user);
    
    List<Achievement> findByIsUnlockedOrderByUnlockedAtDesc(boolean isUnlocked);
}
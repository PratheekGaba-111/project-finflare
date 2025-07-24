package com.finflare.repository;

import com.finflare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u ORDER BY u.totalPoints DESC")
    List<User> findTopUsersByPoints();
    
    @Query("SELECT u FROM User u WHERE u.currentStreak > 0 ORDER BY u.currentStreak DESC")
    List<User> findTopUsersByStreak();
    
    @Query("SELECT u FROM User u WHERE u.enabled = true ORDER BY u.createdAt DESC")
    List<User> findActiveUsers();
}
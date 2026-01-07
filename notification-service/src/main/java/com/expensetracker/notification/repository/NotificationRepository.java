package com.expensetracker.notification.repository;

import com.expensetracker.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserEmailOrderByCreatedAtDesc(String userEmail);
    List<Notification> findByStatus(String status);
    List<Notification> findByUserEmailAndStatus(String userEmail, String status);
    List<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}

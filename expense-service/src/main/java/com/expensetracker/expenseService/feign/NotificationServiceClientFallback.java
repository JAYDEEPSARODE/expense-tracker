package com.expensetracker.expenseService.feign;

import com.expensetracker.expenseService.dto.NotificationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for NotificationServiceClient
 * Handles failures gracefully when notification service is unavailable
 */
@Component
@Slf4j
public class NotificationServiceClientFallback implements NotificationServiceClient {

    @Override
    public ResponseEntity<Object> sendNotification(NotificationRequestDTO request) {
        log.warn("Notification service is unavailable. Fallback triggered for user: {}", 
                request != null ? request.getUserEmail() : "unknown");
        // Return a response indicating the notification was not sent
        // This prevents the expense creation from failing
        return ResponseEntity.status(503).body("Notification service unavailable");
    }
}

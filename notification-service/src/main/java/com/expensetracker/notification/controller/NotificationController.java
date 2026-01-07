package com.expensetracker.notification.controller;

import com.expensetracker.notification.dto.NotificationDTO;
import com.expensetracker.notification.dto.NotificationRequest;
import com.expensetracker.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<NotificationDTO> sendNotification(HttpServletRequest request, 
                                                            @RequestBody NotificationRequest notificationRequest) {
        try {
            NotificationDTO notification = notificationService.sendNotification(notificationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(notification);
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(HttpServletRequest request, 
                                                                          @PathVariable String userEmail) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByUser(userEmail);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error fetching notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByStatus(HttpServletRequest request, 
                                                                          @PathVariable String status) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByStatus(status);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error fetching notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userEmail}/status/{status}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserAndStatus(HttpServletRequest request,
                                                                                 @PathVariable String userEmail,
                                                                                 @PathVariable String status) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByUserAndStatus(userEmail, status);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error fetching notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDTO> getNotificationById(HttpServletRequest request, 
                                                                @PathVariable Long notificationId) {
        try {
            NotificationDTO notification = notificationService.getNotificationById(notificationId);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            log.error("Error fetching notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

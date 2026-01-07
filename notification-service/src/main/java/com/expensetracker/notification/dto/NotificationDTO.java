package com.expensetracker.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long notificationId;
    private String userEmail;
    private String type;
    private String subject;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private String errorMessage;
}

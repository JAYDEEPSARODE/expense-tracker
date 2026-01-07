package com.expensetracker.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String userEmail;
    private String type; // EMAIL, SMS, PUSH
    private String subject;
    private String message;
}

package com.expensetracker.expenseService.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private String userEmail;
    private String type; // EMAIL, SMS, PUSH
    private String subject;
    private String message;
}

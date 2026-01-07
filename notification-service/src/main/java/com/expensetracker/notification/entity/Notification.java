package com.expensetracker.notification.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @Column(name = "USER_EMAIL", nullable = false)
    private String userEmail;

    @Column(name = "TYPE", nullable = false)
    private String type; // EMAIL, SMS, PUSH

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "MESSAGE", nullable = false, length = 2000)
    private String message;

    @Column(name = "STATUS", nullable = false)
    private String status; // PENDING, SENT, FAILED

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "SENT_AT")
    private LocalDateTime sentAt;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;
}

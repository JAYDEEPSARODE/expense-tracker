package com.expensetracker.notification.service;

import com.expensetracker.notification.dto.NotificationDTO;
import com.expensetracker.notification.dto.NotificationRequest;
import com.expensetracker.notification.entity.Notification;
import com.expensetracker.notification.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public NotificationDTO sendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserEmail(request.getUserEmail());
        notification.setType(request.getType());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setStatus("PENDING");
        notification.setCreatedAt(LocalDateTime.now());

        try {
            if ("EMAIL".equalsIgnoreCase(request.getType())) {
                sendEmail(request.getUserEmail(), request.getSubject(), request.getMessage());
            } else if ("SMS".equalsIgnoreCase(request.getType())) {
                sendSMS(request.getUserEmail(), request.getMessage());
            } else if ("PUSH".equalsIgnoreCase(request.getType())) {
                sendPushNotification(request.getUserEmail(), request.getSubject(), request.getMessage());
            }

            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
            notification.setStatus("FAILED");
            notification.setErrorMessage(e.getMessage());
        }

        Notification saved = notificationRepository.save(notification);
        return convertToDTO(saved);
    }

    private void sendEmail(String to, String subject, String message) {
        if (mailSender == null) {
            log.warn("Mail sender not configured. Email notification will not be sent.");
            throw new RuntimeException("Mail sender not configured");
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setFrom("noreply@expensetracker.com");
            mailSender.send(mailMessage);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
            throw e;
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        // Placeholder for SMS service integration
        log.info("SMS would be sent to: {} with message: {}", phoneNumber, message);
        // In a real implementation, integrate with SMS service like Twilio, AWS SNS, etc.
    }

    private void sendPushNotification(String userEmail, String title, String message) {
        // Placeholder for push notification service
        log.info("Push notification would be sent to: {} with title: {} and message: {}", userEmail, title, message);
        // In a real implementation, integrate with FCM, APNS, etc.
    }

    public List<NotificationDTO> getNotificationsByUser(String userEmail) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByUserAndStatus(String userEmail, String status) {
        return notificationRepository.findByUserEmailAndStatus(userEmail, status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return convertToDTO(notification);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(notification.getNotificationId());
        dto.setUserEmail(notification.getUserEmail());
        dto.setType(notification.getType());
        dto.setSubject(notification.getSubject());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setSentAt(notification.getSentAt());
        dto.setErrorMessage(notification.getErrorMessage());
        return dto;
    }
}

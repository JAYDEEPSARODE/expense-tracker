package com.expensetracker.expenseService.service;

import com.expensetracker.expenseService.dto.NotificationRequestDTO;
import com.expensetracker.expenseService.feign.NotificationServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationClient {

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    public void sendNotification(String userEmail, String subject, String message) {
        try {
            NotificationRequestDTO request = new NotificationRequestDTO();
            request.setUserEmail(userEmail);
            request.setType("EMAIL");
            request.setSubject(subject);
            request.setMessage(message);

            ResponseEntity<Object> response = notificationServiceClient.sendNotification(request);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Notification sent successfully to: {}", userEmail);
            } else {
                log.warn("Notification sent but received non-2xx response for user: {}", userEmail);
            }
        } catch (Exception e) {
            log.error("Error sending notification to {}: {}", userEmail, e.getMessage());
            // Don't throw exception - notification failure shouldn't break expense creation
        }
    }
}

package com.expensetracker.expenseService.feign;

import com.expensetracker.expenseService.dto.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client for Notification Service
 * Uses Eureka service discovery to find and communicate with notification-service
 */
@FeignClient(name = "notification-service", path = "/notifications", fallback = NotificationServiceClientFallback.class)
public interface NotificationServiceClient {

    @PostMapping("/send")
    ResponseEntity<Object> sendNotification(@RequestBody NotificationRequestDTO request);
}

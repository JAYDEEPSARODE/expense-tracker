package com.expensetracker.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    private Long auditId;
    private String userEmail;
    private String action;
    private String entityType;
    private String entityId;
    private String requestMethod;
    private String requestUrl;
    private String requestBody;
    private Integer responseStatus;
    private String ipAddress;
    private LocalDateTime timestamp;
}

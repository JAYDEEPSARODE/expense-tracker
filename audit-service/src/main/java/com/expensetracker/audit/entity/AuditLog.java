package com.expensetracker.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUDIT_LOGS")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_log_seq")
    @SequenceGenerator(name = "audit_log_seq", sequenceName = "AUDIT_LOG_SEQ", allocationSize = 1)
    @Column(name = "AUDIT_ID")
    private Long auditId;

    @Column(name = "USER_EMAIL", nullable = false)
    private String userEmail;

    @Column(name = "ACTION", nullable = false)
    private String action;

    @Column(name = "ENTITY_TYPE", nullable = false)
    private String entityType;

    @Column(name = "ENTITY_ID")
    private String entityId;

    @Column(name = "REQUEST_METHOD")
    private String requestMethod;

    @Column(name = "REQUEST_URL")
    private String requestUrl;

    @Column(name = "REQUEST_BODY", length = 4000)
    private String requestBody;

    @Column(name = "RESPONSE_STATUS")
    private Integer responseStatus;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}

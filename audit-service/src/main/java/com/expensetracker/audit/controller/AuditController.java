package com.expensetracker.audit.controller;

import com.expensetracker.audit.dto.AuditLogDTO;
import com.expensetracker.audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/audit")
@Slf4j
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping("/log")
    public ResponseEntity<AuditLogDTO> createAuditLog(@RequestBody AuditLogDTO auditLogDTO) {
        try {
            return ResponseEntity.ok(convertToDTO(auditService.logAction(auditLogDTO)));
        } catch (Exception e) {
            log.error("Error creating audit log: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByUser(@PathVariable String userEmail) {
        try {
            return ResponseEntity.ok(auditService.getAuditLogsByUser(userEmail));
        } catch (Exception e) {
            log.error("Error fetching audit logs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        try {
            return ResponseEntity.ok(auditService.getAuditLogsByEntity(entityType, entityId));
        } catch (Exception e) {
            log.error("Error fetching audit logs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            return ResponseEntity.ok(auditService.getAuditLogsByDateRange(startDate, endDate));
        } catch (Exception e) {
            log.error("Error fetching audit logs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userEmail}/date-range")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByUserAndDateRange(
            @PathVariable String userEmail,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            return ResponseEntity.ok(auditService.getAuditLogsByUserAndDateRange(userEmail, startDate, endDate));
        } catch (Exception e) {
            log.error("Error fetching audit logs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private AuditLogDTO convertToDTO(com.expensetracker.audit.entity.AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setAuditId(auditLog.getAuditId());
        dto.setUserEmail(auditLog.getUserEmail());
        dto.setAction(auditLog.getAction());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setRequestMethod(auditLog.getRequestMethod());
        dto.setRequestUrl(auditLog.getRequestUrl());
        dto.setRequestBody(auditLog.getRequestBody());
        dto.setResponseStatus(auditLog.getResponseStatus());
        dto.setIpAddress(auditLog.getIpAddress());
        dto.setTimestamp(auditLog.getTimestamp());
        return dto;
    }
}

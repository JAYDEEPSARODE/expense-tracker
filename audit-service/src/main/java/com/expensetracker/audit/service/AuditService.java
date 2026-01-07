package com.expensetracker.audit.service;

import com.expensetracker.audit.dto.AuditLogDTO;
import com.expensetracker.audit.entity.AuditLog;
import com.expensetracker.audit.repository.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLog logAction(AuditLogDTO auditLogDTO) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserEmail(auditLogDTO.getUserEmail());
            auditLog.setAction(auditLogDTO.getAction());
            auditLog.setEntityType(auditLogDTO.getEntityType());
            auditLog.setEntityId(auditLogDTO.getEntityId());
            auditLog.setRequestMethod(auditLogDTO.getRequestMethod());
            auditLog.setRequestUrl(auditLogDTO.getRequestUrl());
            auditLog.setRequestBody(auditLogDTO.getRequestBody());
            auditLog.setResponseStatus(auditLogDTO.getResponseStatus());
            auditLog.setIpAddress(auditLogDTO.getIpAddress());
            auditLog.setTimestamp(LocalDateTime.now());
            
            return auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error saving audit log: {}", e.getMessage());
            throw new RuntimeException("Failed to save audit log", e);
        }
    }

    public List<AuditLogDTO> getAuditLogsByUser(String userEmail) {
        return auditLogRepository.findByUserEmailOrderByTimestampDesc(userEmail)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByEntity(String entityType, String entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByUserAndDateRange(String userEmail, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByUserEmailAndTimestampBetween(userEmail, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuditLogDTO convertToDTO(AuditLog auditLog) {
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

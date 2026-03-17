package com.freelanceautomation.backend.audit.service;

import com.freelanceautomation.backend.audit.dto.AuditLogResponse;
import com.freelanceautomation.backend.audit.entity.AuditLog;
import com.freelanceautomation.backend.audit.repository.AuditLogRepository;
import com.freelanceautomation.backend.user.entity.User;
import com.freelanceautomation.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public void logForUser(String email, String action, String details) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .details(details)
                .createdAt(Instant.now())
                .build();

        auditLogRepository.save(log);
    }

    public List<AuditLogResponse> getMyLogs(String email) {
        return auditLogRepository.findByUserEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(log -> new AuditLogResponse(
                        log.getId(),
                        log.getAction(),
                        log.getDetails(),
                        log.getCreatedAt()
                ))
                .toList();
    }
}
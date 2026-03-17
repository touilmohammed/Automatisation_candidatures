package com.freelanceautomation.backend.audit.controller;

import com.freelanceautomation.backend.audit.dto.AuditLogResponse;
import com.freelanceautomation.backend.audit.service.AuditService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/me")
    public List<AuditLogResponse> getMyLogs(Authentication authentication) {
        return auditService.getMyLogs(authentication.getName());
    }
}
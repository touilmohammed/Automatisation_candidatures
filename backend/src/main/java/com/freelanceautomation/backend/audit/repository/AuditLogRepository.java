package com.freelanceautomation.backend.audit.repository;

import com.freelanceautomation.backend.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserEmailOrderByCreatedAtDesc(String email);

}
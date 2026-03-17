package com.freelanceautomation.backend.application.repository;

import com.freelanceautomation.backend.application.entity.ApplicationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRecordRepository extends JpaRepository<ApplicationRecord, Long> {
}
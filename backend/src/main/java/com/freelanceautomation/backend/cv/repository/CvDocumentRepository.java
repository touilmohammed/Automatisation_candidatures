package com.freelanceautomation.backend.cv.repository;

import com.freelanceautomation.backend.cv.entity.CvDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CvDocumentRepository extends JpaRepository<CvDocument, Long> {

    Optional<CvDocument> findByUserEmail(String email);

}
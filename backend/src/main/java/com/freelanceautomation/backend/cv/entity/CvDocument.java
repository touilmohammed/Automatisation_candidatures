package com.freelanceautomation.backend.cv.entity;

import com.freelanceautomation.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "cv_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private String storagePath;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    @Column(nullable = false)
    private Instant uploadedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
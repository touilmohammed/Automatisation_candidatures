package com.freelanceautomation.backend.application.entity;

import com.freelanceautomation.backend.job.entity.JobOffer;
import com.freelanceautomation.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String generatedCv;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String generatedCoverLetter;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Instant reviewedAt;

    @Column
    private Instant submittedAt;

    @Column
    private String submissionMode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;
}
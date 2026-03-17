package com.freelanceautomation.backend.job.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "job_offers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"platform", "external_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String platform;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal budget;

    @Column
    private String currency;

    @Column
    private String location;

    @Column(nullable = false)
    private Boolean remoteAllowed;

    @Column(nullable = false, length = 2000)
    private String jobUrl;

    @Column
    private Instant postedAt;

    @Column(nullable = false)
    private Instant ingestedAt;
}
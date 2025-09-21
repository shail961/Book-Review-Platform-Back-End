package com.BPR.recommendation_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationMetadata {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "last_generated", nullable = false)
    private LocalDateTime lastGenerated;
}
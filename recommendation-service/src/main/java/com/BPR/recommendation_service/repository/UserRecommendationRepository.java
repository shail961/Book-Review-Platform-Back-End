package com.BPR.recommendation_service.repository;

import com.BPR.recommendation_service.entity.UserRecommendation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRecommendationRepository extends org.springframework.data.jpa.repository.JpaRepository<UserRecommendation, UUID> {

    List<UserRecommendation> findTop10ByUserIdOrderByScoreDesc(String userId);

    void deleteByUserId(String userId);
}
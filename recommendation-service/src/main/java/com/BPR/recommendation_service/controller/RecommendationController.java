package com.BPR.recommendation_service.controller;

import com.BPR.recommendation_service.entity.UserRecommendation;
import com.BPR.recommendation_service.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserRecommendation>> getRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(userId));
    }

    @PostMapping("/{userId}/refresh")
    public ResponseEntity<String> refreshRecommendations(@PathVariable String userId) {
        recommendationService.refreshRecommendations(userId);
        return ResponseEntity.ok("Recommendations refreshed for user: " + userId);
    }
}
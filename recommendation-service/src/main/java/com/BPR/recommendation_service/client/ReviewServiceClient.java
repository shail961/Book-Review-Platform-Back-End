package com.BPR.recommendation_service.client;

import com.BPR.recommendation_service.dtos.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "review-service", url = "http://localhost:8083/reviews")
public interface ReviewServiceClient {

    @GetMapping("/user/{userId}")
    List<ReviewResponse> getReviewsByUser(@PathVariable("userId") String userId);
}

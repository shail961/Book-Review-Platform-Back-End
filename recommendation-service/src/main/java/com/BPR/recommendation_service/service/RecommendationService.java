package com.BPR.recommendation_service.service;

import com.BPR.recommendation_service.client.BookServiceClient;
import com.BPR.recommendation_service.client.ReviewServiceClient;
import com.BPR.recommendation_service.dtos.BookResponse;
import com.BPR.recommendation_service.dtos.ReviewCreatedEvent;
import com.BPR.recommendation_service.dtos.ReviewResponse;
import com.BPR.recommendation_service.entity.UserRecommendation;
import com.BPR.recommendation_service.repository.UserRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRecommendationRepository recommendationRepo;

    private final ReviewServiceClient reviewClient;
    private final BookServiceClient bookClient;

    @Cacheable(value = "recommendations", key = "#userId")
    public List<UserRecommendation> getRecommendations(String userId) {
        List<UserRecommendation> existing = recommendationRepo.findTop10ByUserIdOrderByScoreDesc(userId);
        if (existing.isEmpty()) {
            log.info("Computing the recommendation for user : {}",userId);
            List<UserRecommendation> computed = computeRecommendations(userId);
            recommendationRepo.saveAll(computed);
            return computed;
        } else {
            log.info("returning existing data");
        }
        return existing;
    }

    @CacheEvict(value = "recommendations", key = "#userId")
    public void refreshRecommendations(String userId) {
        recommendationRepo.deleteByUserId(userId);// delete data from database and recompute and insert

        // Compute new recommendations immediately
        List<UserRecommendation> computed = computeRecommendations(userId);
        recommendationRepo.saveAll(computed);
    }

    @KafkaListener(topics = "review-created-topic", groupId = "recommendation-service-group")
    public void handleReviewCreated(ReviewCreatedEvent event) {
        log.info("Received ReviewCreatedEvent for userId={}, bookId={}, rating={}",
                event.getUserId(), event.getBookId(), event.getRating());

        // Whenever a review is created, refresh this user’s recommendations
        refreshRecommendations(event.getUserId());
    }

    private List<UserRecommendation> computeRecommendations(String userId) {
        // 1. Fetch user’s reviews
        List<ReviewResponse> reviews = reviewClient.getReviewsByUser(userId);

        if (reviews == null || reviews.isEmpty()) {
            log.info("No reviews given");
            return List.of();
        }

        // 2. Extract genres from reviewed books
        Set<String> favoriteGenres = new HashSet<>();
        for (ReviewResponse review : reviews) {
            UUID bookId = review.getBookId();
            log.info("Book Id: {}", bookId);
            BookResponse book = bookClient.getBook(bookId);
            favoriteGenres.add(book.getGenre());
        }

        // 3. Fetch new books by genre from book-service
        List<UserRecommendation> recommendations = new ArrayList<>();
        for (String genre : favoriteGenres) {
            List<BookResponse> books = bookClient.getBooksByGenre(genre);
            if (books != null) {
                for (BookResponse book : books) {
                    UUID bookId = book.getId();

                    UserRecommendation rec = UserRecommendation.builder()
                            .userId(userId)
                            .bookId(bookId)
                            .score(Math.random() * 5) // simple scoring
                            .createdAt(LocalDateTime.now())
                            .build();
                    recommendations.add(rec);
                }
            }
        }

        return recommendations.stream()
                .sorted(Comparator.comparingDouble(UserRecommendation::getScore).reversed())
                .limit(10)
                .toList();
    }
}
package com.BPR.review_service.service;

import com.BPR.review_service.dtos.ReviewCreatedEvent;
import com.BPR.review_service.entity.Review;
import com.BPR.review_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final KafkaTemplate<String, ReviewCreatedEvent> kafkaTemplate;

    public Review saveReview(String userId, Review review) {
        review.setUserId(userId);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        ReviewCreatedEvent event = new ReviewCreatedEvent();
        event.setReviewId(savedReview.getId());
        event.setBookId(savedReview.getBookId());
        event.setUserId(userId);
        event.setComment(savedReview.getComment());
        event.setRating(savedReview.getRating());
        event.setCreatedAt(savedReview.getCreatedAt());
        log.info("Rating at producer :"+event.getRating());
        kafkaTemplate.send("review-created-topic", event);

        return savedReview;
    }

    public List<Review> getReviewsByBook(UUID bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public List<Review> getReviewsByUser(String userId) {
        return reviewRepository.findByUserId(userId);
    }
}
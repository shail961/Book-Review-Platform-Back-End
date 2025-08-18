package com.BPR.book_service.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewCreatedEvent {
    private UUID reviewId;
    private UUID bookId;
    private String userId;
    private String content;
    private int rating;
    private LocalDateTime createdAt;
}

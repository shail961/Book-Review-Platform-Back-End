package com.BPR.recommendation_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private UUID id;
    private String userId;
    private UUID bookId;
    private int rating;
    private String comment;
    private BookResponse book;
}

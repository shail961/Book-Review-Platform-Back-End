package com.BPR.book_service.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class BookDto {
    private UUID id;
    private String title;
    private String author;
    private String genre;
    private String description;
    private Integer publishedYear;
    private String isbn;
}

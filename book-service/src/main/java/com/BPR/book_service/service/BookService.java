package com.BPR.book_service.service;

import com.BPR.book_service.dtos.BookDto;
import com.BPR.book_service.dtos.ReviewCreatedEvent;
import com.BPR.book_service.entity.Book;
import com.BPR.book_service.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book createBook(BookDto dto, String userId) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .description(dto.getDescription())
                .publishedYear(dto.getPublishedYear())
                .isbn(dto.getIsbn())
                .createdBy(userId)
                .build();

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @KafkaListener(topics = "review-created-topic", groupId = "book-service-group")
    @Transactional
    public void handleReviewCreated(ReviewCreatedEvent event) {
        bookRepository.findById(event.getBookId()).ifPresent(book -> {
            // Update average rating
            int newTotalReviews = book.getTotalReviews() + 1;
            double newAvgRating = ((book.getAvgRating() * book.getTotalReviews()) + event.getRating())
                    / newTotalReviews;

            book.setTotalReviews(newTotalReviews);
            book.setAvgRating(newAvgRating);

            bookRepository.save(book);
        });
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }
}
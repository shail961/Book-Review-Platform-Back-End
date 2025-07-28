package com.BPR.book_service.service;

import com.BPR.book_service.dtos.BookDto;
import com.BPR.book_service.entity.Book;
import com.BPR.book_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
}
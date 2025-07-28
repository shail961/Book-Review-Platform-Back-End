package com.BPR.book_service.service;

import com.BPR.book_service.entity.Book;
import com.BPR.book_service.entity.UserFavorite;
import com.BPR.book_service.repository.BookRepository;
import com.BPR.book_service.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;

    public void addToFavorites(String userId, UUID bookId) {
        if (favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new IllegalArgumentException("Already in favorites");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setBook(book);

        favoriteRepository.save(favorite);
    }

    public void removeFromFavorites(String userId, UUID bookId) {
        favoriteRepository.deleteByUserIdAndBookId(userId, bookId);
    }

    public List<Book> getUserFavorites(String userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(UserFavorite::getBook)
                .collect(Collectors.toList());
    }
}
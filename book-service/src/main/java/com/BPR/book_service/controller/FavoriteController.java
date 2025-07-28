package com.BPR.book_service.controller;

import com.BPR.book_service.entity.Book;
import com.BPR.book_service.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{bookId}")
    public ResponseEntity<String> addToFavorites(@PathVariable UUID bookId,
                                                 @RequestAttribute("userId") String userId) {
        favoriteService.addToFavorites(userId, bookId);
        return ResponseEntity.ok("Added to favorites");
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable UUID bookId,
                                                      @RequestAttribute("userId") String userId) {
        favoriteService.removeFromFavorites(userId, bookId);
        return ResponseEntity.ok("Removed from favorites");
    }

    @GetMapping
    public ResponseEntity<List<Book>> getFavorites(@RequestAttribute("userId") String userId) {
        List<Book> favorites = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }
}

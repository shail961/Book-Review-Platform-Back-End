package com.BPR.recommendation_service.client;

import com.BPR.recommendation_service.dtos.BookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "book-service", url = "http://localhost:8082/books")
public interface BookServiceClient {

    @GetMapping("/genre/{genre}")
    List<BookResponse> getBooksByGenre(@PathVariable("genre") String genre);

    @GetMapping("/{id}")
    BookResponse getBook(@PathVariable("id")  UUID id) ;
}
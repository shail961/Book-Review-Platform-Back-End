package com.BPR.book_service.repository;

import com.BPR.book_service.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<UserFavorite, UUID> {

    List<UserFavorite> findByUserId(String userId);

    boolean existsByUserIdAndBookId(String userId, UUID bookId);

    void deleteByUserIdAndBookId(String userId, UUID bookId);
}
package com.fsad.bookservice.repository;

import com.fsad.bookservice.entities.Wishlist;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
  Page<Wishlist> findByUserId(Long userId, Pageable pageable);

  @Modifying
  @Transactional
  void deleteByBookIdAndUserId(Long bookId, Long userId);
}

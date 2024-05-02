package com.fsad.bookservice.service;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.dto.WishlistDTO;
import com.fsad.bookservice.entities.Book;
import com.fsad.bookservice.entities.Wishlist;
import com.fsad.bookservice.repository.BookRepository;
import com.fsad.bookservice.repository.WishlistRepository;
import com.fsad.bookservice.utils.BookConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WishlistService {

  @Autowired
  private WishlistRepository wishlistRepository;

  @Autowired
  private BookRepository bookRepository;

  public WishlistDTO getWishlist(Long userId, int page, int size) {
    WishlistDTO wishlistDTO = WishlistDTO.builder().build();
    Pageable pageable = PageRequest.of(page, size);
    Page<Wishlist> wishlists = wishlistRepository.findByUserId(userId, pageable);
    if (wishlists.getTotalElements() > 0) {
      List<Long> bookIds = wishlists.getContent().stream().map(Wishlist::getBookId).toList();
      List<Book> books = bookRepository.findAllById(bookIds);
      List<BookDTO> bookDTOS = new ArrayList<>();
      for (Book book : books) {
        BookDTO bookDTO = BookConvertor.toDTO(book);
        bookDTO.setInWishlist(true);
        bookDTOS.add(bookDTO);
      }
      wishlistDTO.setBookDTOs(bookDTOS);
      wishlistDTO.setSize(size);
      wishlistDTO.setPage(page);
      wishlistDTO.setTotalPages(wishlists.getTotalPages());
      wishlistDTO.setTotalElements(wishlists.getTotalElements());
    }
    return wishlistDTO;
  }

  public void saveToWishList(Long bookId, Long userId) {
    Wishlist wishlist = Wishlist.builder()
        .bookId(bookId)
        .userId(userId)
        .build();
    wishlistRepository.save(wishlist);
  }

  public void removeFromWishlist(Long bookId, Long userId) {
    log.info("deleting from wishlist bookId : {}, userId : {}", bookId, userId);
    wishlistRepository.deleteByBookIdAndUserId(bookId, userId);
  }
}

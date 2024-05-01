package com.fsad.bookservice.service;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.dto.SearchResponseDTO;
import com.fsad.bookservice.entities.Book;
import com.fsad.bookservice.entities.Wishlist;
import com.fsad.bookservice.enums.SearchFilter;
import com.fsad.bookservice.model.SearchFilterParams;
import com.fsad.bookservice.repository.BookRepository;
import com.fsad.bookservice.repository.WishlistRepository;
import com.fsad.bookservice.utils.BookConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private WishlistRepository wishlistRepository;

  public SearchResponseDTO search(Map<String, String> allParams, Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Book> bookPage = bookRepository.findAll(getBookSpecification(allParams, userId), pageable);
    SearchResponseDTO searchResponseDTO = SearchResponseDTO.builder()
        .page(bookPage.getNumber())
        .size(bookPage.getSize())
        .totalPages(bookPage.getTotalPages())
        .totalElements(bookPage.getTotalElements())
        .build();

    if (bookPage.getTotalElements() > 0) {

      // Todo : Call only for bookIds which are present in page - findByUserIdAndBookIdIn(..).
      List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
      Set<Long> bookIdsInWishlist = wishlists.stream().map(Wishlist::getBookId).collect(Collectors.toSet());

      List<BookDTO> bookDTOS = new ArrayList<>();
      for (Book book : bookPage.getContent()) {
        BookDTO bookDTO = BookConvertor.toDTO(book);
        if (bookIdsInWishlist.contains(book.getId())) {
          bookDTO.setInWishlist(true);
        }
        bookDTOS.add(bookDTO);
      }

      searchResponseDTO.setBookDTOs(bookDTOS);
    } else {
      searchResponseDTO.setBookDTOs(Collections.emptyList());
    }
    return searchResponseDTO;
  }

  private BookSpecification getBookSpecification(Map<String, String> allParams, Long userId) {
    String genre = allParams.get(SearchFilter.GENRE.getName());
    String author = allParams.get(SearchFilter.AUTHOR.getName());
    String title = allParams.get(SearchFilter.TITLE.getName());
    SearchFilterParams params = SearchFilterParams.builder()
        .genre(genre)
        .author(author)
        .title(title)
        .userID(userId)
        .build();
    return new BookSpecification(params);
  }
}

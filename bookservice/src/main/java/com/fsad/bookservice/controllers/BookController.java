package com.fsad.bookservice.controllers;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.entities.Book;
import com.fsad.bookservice.repository.BookRepository;
import com.fsad.bookservice.utils.BookConvertor;
import com.fsad.bookservice.utils.BookUtil;
import com.fsad.bookservice.utils.Patcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BookUtil bookUtil;

  @Autowired
  private Patcher patcher;

  private final Logger logger = LoggerFactory.getLogger(BookController.class);

  @CrossOrigin
  @PostMapping("/books")
  public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO,
                                         @RequestHeader("Authorization") String token) {
    try {
      ResponseEntity<Long> response = bookUtil.validateToken(token);
      if (response.getStatusCode() == HttpStatus.OK) {
        bookDTO.setUserID(response.getBody());
        Book book = BookConvertor.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        BookDTO dto = BookConvertor.toDTO(savedBook);

        return new ResponseEntity<>(dto, HttpStatus.OK);
      }
    } catch (Exception e) {
      logger.error("Failed to add book", e);
      if (e instanceof HttpClientErrorException) {
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @CrossOrigin
  @GetMapping("/books")
  public ResponseEntity<List<BookDTO>> getAllBooksForUser(@RequestHeader("Authorization") String token) {

    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {
      List<Book> books = bookRepository.findBookByUserID(response.getBody());
      List<BookDTO> bookDTOS = new ArrayList<>();
      books.forEach(book -> bookDTOS.add(BookConvertor.toDTO(book)));
      return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @CrossOrigin
  @PatchMapping("/books/{bookId}")
  public ResponseEntity<BookDTO> updateBook(@PathVariable("bookId") Long bookId,
                                            @RequestBody BookDTO bookDTO,
                                            @RequestHeader("Authorization") String token) throws IllegalAccessException {
    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {
      Optional<Book> book = bookRepository.findById(bookId);
      if (book.isPresent()) {
        Book existingBook = book.get();
        boolean updated = patcher.bookPatcher(existingBook, BookConvertor.toEntity(bookDTO));
        if (updated) {
          existingBook = bookRepository.save(existingBook);
          return new ResponseEntity<>(BookConvertor.toDTO(existingBook), HttpStatus.OK);
        } else {
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
      }
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @CrossOrigin
  @DeleteMapping("/books/{bookId}")
  public ResponseEntity<Void> removeBook(@PathVariable("bookId") Long bookId,
                                         @RequestHeader("Authorization") String token) {
    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {
      Optional<Book> book = bookRepository.findById(bookId);
      if (book.isPresent()) {
        Book toBeDeleted = book.get();
        if (Objects.equals(toBeDeleted.getUserID(), response.getBody())) {
          bookRepository.delete(toBeDeleted);
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
      }
    }
    return new ResponseEntity<>(response.getStatusCode());
  }
}

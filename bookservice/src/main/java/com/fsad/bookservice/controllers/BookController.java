package com.fsad.bookservice.controllers;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {
  @Autowired
  private BookService bookService;
  private final Logger logger = LoggerFactory.getLogger(BookController.class);

  @PostMapping("/books")
  public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/books/{bookId}")
  public ResponseEntity<Void> removeBook(@RequestParam("bookId") Long bookId) {
    bookService.removeBook(bookId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

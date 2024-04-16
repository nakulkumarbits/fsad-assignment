package com.fsad.bookservice.service;

import com.fsad.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  public void removeBook(Long bookId) {
    bookRepository.deleteById(bookId);
  }
}

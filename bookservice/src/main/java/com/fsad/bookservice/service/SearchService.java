package com.fsad.bookservice.service;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

  @Autowired
  private BookRepository bookRepository;

  public List<BookDTO> search(Map<String, String> allParams) {
    String genre = allParams.get("genre");
    String author = allParams.get("author");
    String title = allParams.get("title");
    bookRepository.findByGenreAndAuthorAndTitle(genre, author, title);
    return null;
  }
}

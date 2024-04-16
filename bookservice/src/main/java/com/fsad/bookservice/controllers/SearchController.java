package com.fsad.bookservice.controllers;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

  @Autowired
  private SearchService searchService;

  @GetMapping("/search")
  public ResponseEntity<List<BookDTO>> getBooks(@RequestParam Map<String, String> allParams) {
    return new ResponseEntity<>(searchService.search(allParams), HttpStatus.OK);
  }
}

package com.fsad.bookservice.controllers;

import com.fsad.bookservice.dto.SearchResponseDTO;
import com.fsad.bookservice.service.SearchService;
import com.fsad.bookservice.utils.BookUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@RestController
@Slf4j
public class SearchController {

  @Autowired
  private SearchService searchService;

  @Autowired
  private BookUtil bookUtil;

  @CrossOrigin
  @GetMapping("/search")
  public ResponseEntity<SearchResponseDTO> getBooks(@RequestParam Map<String, String> allParams,
                                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                                    @RequestParam(name = "size", defaultValue = "10") int size,
                                                    @RequestHeader("Authorization") String token) {
    try {
      log.info("[getBooks] all params : {}, page : {}, size : {}", allParams, page, size);
      ResponseEntity<Long> response = bookUtil.validateToken(token);
      if (response.getStatusCode() == HttpStatus.OK) {
        SearchResponseDTO searchResponseDTO = searchService.search(allParams, response.getBody(), page, size);
//        log.info("[getBooks] searchResponseDTO : {}", searchResponseDTO);
        return new ResponseEntity<>(searchResponseDTO, HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error("[getBooks] Failed to search book", e);
      if (e instanceof HttpClientErrorException) {
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }
}

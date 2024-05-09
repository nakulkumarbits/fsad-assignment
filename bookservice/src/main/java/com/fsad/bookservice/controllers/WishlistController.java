package com.fsad.bookservice.controllers;

import com.fsad.bookservice.dto.WishlistDTO;
import com.fsad.bookservice.service.WishlistService;
import com.fsad.bookservice.utils.BookUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@Slf4j
public class WishlistController {

  @Autowired
  private BookUtil bookUtil;

  @Autowired
  private WishlistService wishlistService;

  @Operation(summary = "Fetch books which are in wishlist for the logged in user.")
  @GetMapping("/wishlist")
  public ResponseEntity<WishlistDTO> getBooksFromWishlist(@RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "10") int size,
                                                          @RequestHeader("Authorization") String token) {
    try {
      ResponseEntity<Long> response = bookUtil.validateToken(token);
      if (response.getStatusCode() == HttpStatus.OK) {
        WishlistDTO wishlist = wishlistService.getWishlist(response.getBody(), page, size);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error("[getBooksFromWishlist] Failed to fetch wishlist: ", e);
      if (e instanceof HttpClientErrorException) {
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Add book in wishlist for the logged in user.")
  @PostMapping("/wishlist/{bookId}")
  public ResponseEntity<Void> addBookToWishlist(@PathVariable("bookId") Long bookId,
                                                @RequestHeader("Authorization") String token) {
    try {
      ResponseEntity<Long> response = bookUtil.validateToken(token);
      if (response.getStatusCode() == HttpStatus.OK) {
        wishlistService.saveToWishList(bookId, response.getBody());
        return new ResponseEntity<>(HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error("[addBookToWishlist] Failed to add to wishlist: ", e);
      if (e instanceof HttpClientErrorException) {
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Remove book from wishlist for the logged in user.")
  @DeleteMapping("/wishlist/{bookId}")
  public ResponseEntity<Void> removeBookFromWishlist(@PathVariable("bookId") Long bookId,
                                                     @RequestHeader("Authorization") String token) {
    try {
      ResponseEntity<Long> response = bookUtil.validateToken(token);
      if (response.getStatusCode() == HttpStatus.OK) {
        wishlistService.removeFromWishlist(bookId, response.getBody());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
    } catch (Exception e) {
      log.error("[addBookToWishlist] Failed to add to wishlist: ", e);
      if (e instanceof HttpClientErrorException) {
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }
}

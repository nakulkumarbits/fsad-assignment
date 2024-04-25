package com.fsad.bookservice.controllers;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.entities.Book;
import com.fsad.bookservice.entities.OrderHistory;
import com.fsad.bookservice.enums.DeliveryMode;
import com.fsad.bookservice.repository.BookRepository;
import com.fsad.bookservice.repository.OrderHistoryRepository;
import com.fsad.bookservice.utils.BookConvertor;
import com.fsad.bookservice.utils.BookUtil;
import com.fsad.bookservice.utils.Patcher;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fsad.bookservice.enums.Action.EXCHANGE;

@RestController
public class BookController {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  OrderHistoryRepository orderHistoryRepository;

  @Autowired
  BookUtil bookUtil;

  @Autowired
  Patcher patcher;

  private final Logger logger = LoggerFactory.getLogger(BookController.class);

  @PostMapping("/add")
  public ResponseEntity<List<BookDTO>> addBook(@RequestBody List<BookDTO> bookDTOS,
                                            @RequestHeader("Authorization") String token) {
    try{
      ResponseEntity<Long> response = bookUtil.validate(token);
      if(response.getStatusCode() == HttpStatus.OK)
      {
        List<Book> books = new ArrayList<>();
        bookDTOS.forEach(bookDTO -> {
          bookDTO.setUserID(response.getBody());
          books.add(BookConvertor.toEntity(bookDTO));
        });
        List<Book> createdBooks = bookRepository.saveAll(books);

        List<BookDTO> createdBookDTOS = new ArrayList<>();
        createdBooks.forEach(book -> createdBookDTOS.add(BookConvertor.toDTO(book)));

        return new ResponseEntity<>(createdBookDTOS, HttpStatus.OK);
      }
    }
    catch(Exception e)
    {
      logger.error("Failed to add book", e);
      if(e instanceof HttpClientErrorException){
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @GetMapping()
  public ResponseEntity<List<BookDTO>> findAll(@RequestHeader("Authorization") String token) {

    ResponseEntity<Long> response = bookUtil.validate(token);
    if(response.getStatusCode() == HttpStatus.OK)
    {
      List<Book> books = bookRepository.findBookByUserID(response.getBody());
      List<BookDTO> bookDTOS = new ArrayList<>();
      books.forEach(book -> bookDTOS.add(BookConvertor.toDTO(book)));
      return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @PatchMapping("/{bookId}")
  public ResponseEntity<BookDTO> updateBook(@PathVariable("bookId") long bookId,
                                         @RequestBody BookDTO bookDTOPatch,
                                         @RequestHeader("Authorization") String token) throws IllegalAccessException {
    ResponseEntity<Long> response = bookUtil.validate(token);
    if(response.getStatusCode() == HttpStatus.OK)
    {
      Optional<Book> book = bookRepository.findById(bookId);
      if(book.isPresent())
      {
        Book existingBook = book.get();
        boolean updated = patcher.bookPatcher(existingBook, BookConvertor.toEntity(bookDTOPatch));
        if(updated)
        {
          existingBook = bookRepository.save(existingBook);
          return new ResponseEntity<>(BookConvertor.toDTO(existingBook), HttpStatus.OK);
        }
        else {
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
      }
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }
    else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<Void> removeBook(@PathVariable long bookId,
                                         @RequestHeader("Authorization") String token) {
    ResponseEntity<Long> response = bookUtil.validate(token);
    if(response.getStatusCode() == HttpStatus.OK)
    {
      Optional<Book> book = bookRepository.findById(bookId);
      if(book.isPresent())
      {
        Book toBeDeleted = book.get();
        if(Objects.equals(toBeDeleted.getUserID(), response.getBody()))
        {
          bookRepository.delete(toBeDeleted);
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
      }
    }
      return new ResponseEntity<>(response.getStatusCode());
  }

  @PostMapping("/exchange")
  public ResponseEntity<Void> exchangeBook(@RequestBody String changeRequest,
                                           @RequestHeader("Authorization") String token){
    ResponseEntity<Long> response = bookUtil.validate(token);
    if(response.getStatusCode() == HttpStatus.OK){
      JSONObject jsonObject = new JSONObject(changeRequest);
      long requesterBookID = jsonObject.getLong("requesterBookID");
      long requestingBookID = jsonObject.getLong("requestingBookID");
      int duration = jsonObject.getInt("duration");
      String modeOfDelivery = jsonObject.has("modeOfDelivery") ? jsonObject.getString("modeOfDelivery") : "null";

      Optional<Book> requesterBook = bookRepository.findById(requesterBookID);
      if(requesterBook.isPresent())
      {
        Book requesterBookObj = requesterBook.get();
        if(Objects.equals(requesterBookObj.getUserID(), response.getBody()))
        {
          Optional<Book> requestingBook = bookRepository.findById(requestingBookID);
          if(requestingBook.isPresent())
          {
            Book requestingBookObj = requestingBook.get();
            OrderHistory orderHistory = new OrderHistory(
                    requestingBookObj.getUserID(),
                    requestingBookObj.getId(),
                    requesterBookObj.getUserID(),
                    requesterBookObj.getId(),
                    EXCHANGE,
                    duration,
                    modeOfDelivery.equals("null") ? DeliveryMode.STANDARD:DeliveryMode.valueOf(modeOfDelivery.toUpperCase()));
            orderHistoryRepository.save(orderHistory);
            return new ResponseEntity<>(HttpStatus.OK);
          }
          else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }
        }
        else
        {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

      }
      else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
    return new ResponseEntity<>(response.getStatusCode());
  }


}

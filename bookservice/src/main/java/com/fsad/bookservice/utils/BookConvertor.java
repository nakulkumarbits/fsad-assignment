package com.fsad.bookservice.utils;

import com.fsad.bookservice.dto.BookDTO;
import com.fsad.bookservice.entities.Book;

public class BookConvertor {

  public static Book toEntity(BookDTO bookDTO) {
    if (bookDTO == null) {
      return null;
    }
    Book book = new Book();
    book.setId(bookDTO.getId());
    book.setTitle(bookDTO.getTitle());
    book.setAuthor(bookDTO.getAuthor());
    book.setPublisher(bookDTO.getPublisher());
    book.setBookCondition(bookDTO.getBookCondition());
    book.setGenre(bookDTO.getGenre());
    book.setUserID(bookDTO.getUserID());
    return book;
  }

  public static BookDTO toDTO(Book book) {
    if (book == null) {
      return null;
    }
    BookDTO bookDTO = new BookDTO();
    bookDTO.setId(book.getId());
    bookDTO.setTitle(book.getTitle());
    bookDTO.setAuthor(book.getAuthor());
    bookDTO.setPublisher(book.getPublisher());
    bookDTO.setBookCondition(book.getBookCondition());
    bookDTO.setGenre(book.getGenre());
    bookDTO.setUserID(book.getUserID());

    return bookDTO;
  }
}
